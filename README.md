# Netflix DGS Social Media Service

## Introduction
GraphQL is an exciting query language for APIs that empowers clients to specify the exact data they need. It is an excellent choice for building social media services, where users often require specific data, such as a list of posts by a particular user or the latest posts from a specific topic.

In this project, we will develop a dynamic social media service using Netflix DGS. This service will enable users to register, create posts, delete posts, and subscribe to posts.

The application will utilize a MySQL database for data persistence and builtin subscription support for handling GraphQL subscriptions.

## Installation

### Prerequisites

- JDK 17
- Docker
- A Text Editor or an IDE (IntelliJ IDEA recommended)

### Steps

1. Clone the repository
```bash
  git clone https://github.com/aashikam/dgs-social-media-app.git
```

2. Navigate to the project directory
```bash
  cd dgs-social-media-app
```

3. Start the MySQL database using Docker Compose
```bash
  docker-compose up -d
```

4. Run the application
```bash
  ./gradlew bootRun
```

### Testing the application

1. Open the GraphQL Playground at http://localhost:8080/graphiql
