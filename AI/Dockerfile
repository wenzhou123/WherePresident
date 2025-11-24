FROM python:3.12-slim

WORKDIR /app

# Install system dependencies if needed
RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list.d/debian.sources && \
    apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements first to leverage cache
COPY langchain/requirements.txt langchain/requirements.txt
COPY langserver/requirements.txt langserver/requirements.txt

# Install dependencies using Tsinghua PyPI mirror
ENV PIP_INDEX_URL=https://pypi.tuna.tsinghua.edu.cn/simple
RUN pip install --no-cache-dir -i $PIP_INDEX_URL -r langchain/requirements.txt
RUN pip install --no-cache-dir -i $PIP_INDEX_URL -r langserver/requirements.txt

# Copy source code
COPY langchain ./langchain
COPY langserver ./langserver

# Expose port
EXPOSE 8000

# Set PYTHONPATH to include current directory
ENV PYTHONPATH=/app

# Run the server
CMD ["python", "langserver/main.py"]
