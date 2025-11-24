import os
import json
from datetime import datetime
from langchain_openai import ChatOpenAI
from langchain_core.prompts import PromptTemplate
from langchain_core.messages import HumanMessage
from dotenv import load_dotenv

load_dotenv()

# Configure DeepSeek via OpenAI compatible interface
# NOTE: User needs to provide DEEPSEEK_API_KEY in .env or environment
DEEPSEEK_API_KEY = os.getenv("DEEPSEEK_API_KEY")
if not DEEPSEEK_API_KEY:
    raise ValueError("DEEPSEEK_API_KEY environment variable is not set")
DEEPSEEK_BASE_URL = "https://api.deepseek.com"

llm = ChatOpenAI(
    model="deepseek-chat",
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL,
    temperature=0.7
)

def get_leader_info(country: str):
    """
    Queries the LLM to find the leader of the given country and their current location/itinerary.
    Returns a JSON object with the details.
    """
    prompt = f"""
    You are a real-time intelligence agent tracking world leaders.
    The current date is {datetime.now().strftime("%Y-%m-%d")}.
    
    Who is the current leader of {country}? 
    Where are they right now (today/this week)?
    
    Search for their LATEST schedule, visits, or official location as of {datetime.now().strftime("%B %Y")}.
    If they are not on a foreign trip, assume they are at their official residence/capital.
    
    Please return the response in strict JSON format with the following fields:
    - leader_name: Name of the leader
    - position: Title/Position (e.g., President, Prime Minister)
    - current_location: City or specific place they are currently in
    - description: Brief description of what they are doing there (e.g., "Attending G20 Summit", "Official visit to X", "At official residence")
    - start_date: Estimated start date of this visit (YYYY-MM-DD HH:mm:ss), use current time if unknown
    - end_date: Estimated end date of this visit (YYYY-MM-DD HH:mm:ss), use 2 hours from now if unknown
    - latitude: Estimated latitude of the location (float)
    - longitude: Estimated longitude of the location (float)
    
    Do not include any markdown formatting like ```json ... ```. Just the raw JSON string.
    """
    
    try:
        print(f"Invoking LLM for country: {country}")
        response = llm.invoke([HumanMessage(content=prompt)])
        print(f"Raw LLM response: {response}")
        
        # Parse JSON from response
        output = response.content.strip()
        # Clean up markdown code blocks if present
        if "```json" in output:
            output = output.split("```json")[1].split("```")[0].strip()
        elif "```" in output: # Fallback for generic markdown block
            output = output.split("```")[1].strip()
            
        print(f"Parsed JSON string: {output}")
        return json.loads(output)
    except Exception as e:
        print(f"Error in get_leader_info: {e}")
        # Return a fallback/error structure
        return {
            "error": str(e),
            "country": country
        }

if __name__ == "__main__":
    # Test locally
    print(get_leader_info("France"))
