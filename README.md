# Content Based Image Retrieval System

## Getting Started

### Prerequisites

Before you begin, ensure you have the following tools installed on your system:

- [`maven`](https://maven.apache.org/)
- [`jdk 21`](https://openjdk.org/)

### Building & Running

Follow the steps below to compile and run the program:

1. Clone repository:

    ```bash
    git clone https://github.com/michael-re/content-retrieval.git
    ```

2. Navigate to project directory:

    ```bash
    cd content-retrieval/
    ```

3. Build:

    ```bash
    # build
    mvn clean package

    # clean up build artifacts
    mvn clean
    ```

4. Run:

    ```bash
    java -jar target/cbir.jar
    ```
