import sys
import os
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional
from datetime import datetime, timedelta

# Add langchain directory to path so we can import agent
sys.path.append(os.path.join(os.path.dirname(__file__), "../langchain"))

from agent import get_leader_info

app = FastAPI(title="President Tracker AI Service")

class LeaderRequest(BaseModel):
    country: str

class LeaderResponse(BaseModel):
    leader_name: str
    position: str
    current_location: str
    description: str
    start_date: str
    end_date: str
    latitude: float
    longitude: float
    country: str

@app.post("/api/track-leader", response_model=LeaderResponse)
async def track_leader(request: LeaderRequest):
    print(f"Received track-leader request for: {request.country}")
    try:
        info = get_leader_info(request.country)
        if not info:
            print("Failed to get leader info from agent")
            raise HTTPException(status_code=500, detail="Failed to fetch leader info")
        
        if "error" in info:
            print(f"Agent returned error: {info['error']}")
            raise HTTPException(status_code=500, detail=info["error"])
        
        print(f"Returning leader info: {info}")
        
        # Default values for missing fields
        now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        later = (datetime.now() + timedelta(hours=2)).strftime("%Y-%m-%d %H:%M:%S")
        
        return LeaderResponse(
            leader_name=info.get("leader_name", "Unknown"),
            position=info.get("position", "Unknown"),
            current_location=info.get("current_location", "Unknown"),
            description=info.get("description", "No information available"),
            start_date=info.get("start_date", now),
            end_date=info.get("end_date", later),
            latitude=info.get("latitude", 0.0),
            longitude=info.get("longitude", 0.0),
            country=request.country
        )
    except HTTPException as he:
        raise he
    except Exception as e:
        print(f"An error occurred: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "ok"}

@app.get("/")
async def root():
    return {"message": "President Tracker AI Service is running. Use POST /api/track-leader to track a leader."}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
