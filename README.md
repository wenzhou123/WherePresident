# WherePresident

**Where Is President** is a full-stack application designed to track the location and itinerary of world leaders using AI. It consists of a Spring Boot backend, a React frontend, and a Python-based AI service powered by DeepSeek.

## 🚀 One-Click Deployment

The easiest way to run the entire application is using Docker Compose.

### Prerequisites

- [Docker](https://www.docker.com/products/docker-desktop) installed and running.
- [Git](https://git-scm.com/) installed.
- A **DeepSeek API Key**.

### Quick Start

1.  **Clone the repository** (if you haven't already):
    ```bash
    git clone <repository-url>
    cd WherePresident
    ```

2.  **Set your API Key**:
    You need to provide your DeepSeek API key to the AI service. You can do this by setting an environment variable in your terminal before running the command:

    **Windows (PowerShell):**
    ```powershell
    $env:DEEPSEEK_API_KEY="your_actual_api_key_here"
    docker-compose up --build
    ```

    **Mac/Linux:**
    ```bash
    export DEEPSEEK_API_KEY=your_actual_api_key_here
    docker-compose up --build
    ```

    *Alternatively, you can create a `.env` file in the root directory (same level as `docker-compose.yml`) with the following content:*
    ```env
    DEEPSEEK_API_KEY=your_actual_api_key_here
    ```

3.  **Access the Application**:
    Once the services are up and running (this may take a few minutes for the first build), access the application at:
    - **Frontend**: [http://localhost:80](http://localhost:80)
    - **Backend API**: [http://localhost:8080](http://localhost:8080)
    - **AI Service**: [http://localhost:8000](http://localhost:8000)

4.  **Stop the Application**:
    Press `Ctrl+C` in the terminal or run:
    ```bash
    docker-compose down
    ```

---

## 📂 Project Structure

- **`frontend/`**: React application (Vite) for the user interface.
- **`backend/`**: Spring Boot application for data persistence and API management.
- **`AI/`**: Python service using LangChain and DeepSeek to fetch and process leader data.
- **`docker-compose.yml`**: Orchestration file for running all services together.

## 🛠 Configuration

### Environment Variables

| Service | Variable | Description | Required |
| :--- | :--- | :--- | :--- |
| `ai-service` | `DEEPSEEK_API_KEY` | API key for DeepSeek LLM. | **Yes** |
| `backend` | `SPRING_DATASOURCE_URL` | Database URL (default: H2 in-memory). | No |

## 🔧 Manual Startup (Development)

If you prefer to run services individually for development:

### 1. AI Service
```bash
cd AI
pip install -r langchain/requirements.txt
pip install -r langserver/requirements.txt
export DEEPSEEK_API_KEY=your_key
python langserver/main.py
```

### 2. Backend
```bash
cd backend
./mvnw spring-boot:run
```

### 3. Frontend
```bash
cd frontend
npm install
npm run dev
```
