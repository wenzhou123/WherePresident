import sys
import os
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional

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
        
        print(f"Returning leader info: {info}")
        return LeaderResponse(
            leader_name=info.get("leader_name"),
            position=info.get("position"),
            current_location=info.get("current_location"),
            description=info.get("description"),
            start_date=info.get("start_date"),
            end_date=info.get("end_date"),
            latitude=info.get("latitude"),
            longitude=info.get("longitude"),
            country=request.country
        )
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
