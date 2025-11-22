# AI Leader Tracker Service

This service uses DeepSeek (via LangChain) to track world leaders and update the backend database.

## Structure
- `langchain/`: Contains the agent logic (`agent.py`).
- `langserver/`: Contains the FastAPI server (`main.py`) exposing the agent.

## Setup

1.  **Install Dependencies**:
    ```bash
    pip install -r langchain/requirements.txt
    pip install -r langserver/requirements.txt
    ```

2.  **Set API Key**:
    Set the `DEEPSEEK_API_KEY` environment variable. You can create a `.env` file in `AI/langchain` or export it in your shell.
    ```bash
    export DEEPSEEK_API_KEY=your_key_here
    ```

## Running the Service

Start the LangServer:
```bash
python langserver/main.py
```
The server will run on `http://localhost:8000`.

## Triggering Updates

The backend exposes an endpoint to trigger updates for a specific country:

```bash
curl -X POST http://localhost:8080/api/itineraries/update \
  -H "Content-Type: application/json" \
  -d '{"country": "France"}'
```

This will:
1.  Call the LangServer to find the leader of France.
2.  Receive the itinerary data.
3.  Update the H2 database with the new leader/itinerary info.
