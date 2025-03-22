# Blog API Specification Document

This document outlines the API endpoints for the Blog Platform backend, implemented with Spring Boot 3.

## Base URL

```rest
http://localhost:8080/api
```

## Authentication

Most endpoints require authentication using JWT (JSON Web Token).

Include the token in the `Authorization` header as follows:

```rest
Authorization: Bearer <your_jwt_token>
```

## API Endpoints

### Posts

#### Get All Posts

Retrieves a paginated list of blog posts.

```rest
GET /posts
```

**Query Parameters:**

- `page` (optional): Page number (default: 0)
- `size` (optional): Number of items per page (default: 10)
- `sort` (optional): Field to sort by (default: "createdAt,desc")
- `category` (optional): Filter by category
- `tag` (optional): Filter by tag

**Response:**

```json
{
  "content": [
    {
      "id": 1,
      "title": "Sample Blog Post",
      "slug": "sample-blog-post",
      "description": "This is a short description of the blog post",
      "content": "Full content of the blog post...",
      "imageUrl": "https://example.com/images/sample.jpg",
      "date": "2023-12-15T10:30:00",
      "author": "John Doe",
      "tags": ["spring", "java", "web"]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 42,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

**Status Codes:**

- 200: Success
- 401: Unauthorized
- 500: Server Error

#### Get Post by Slug

Retrieves a single blog post by its slug.

```rest
GET /posts/{slug}
```

**Path Parameters:**

- `slug`: The slug of the post

**Response:**

```json
{
  "id": 1,
  "title": "Sample Blog Post",
  "slug": "sample-blog-post",
  "description": "This is a short description of the blog post",
  "content": "Full content of the blog post...",
  "imageUrl": "https://example.com/images/sample.jpg",
  "date": "2023-12-15T10:30:00",
  "author": "John Doe",
  "tags": ["spring", "java", "web"],
  "featuredPosts": [
    {
      "id": 2,
      "title": "Related Post 1",
      "slug": "related-post-1",
      "description": "Description of related post",
      "imageUrl": "https://example.com/images/related1.jpg",
      "date": "2023-12-10T08:45:00"
    }
  ]
}
```

**Status Codes:**

- 200: Success
- 404: Post not found
- 500: Server Error

#### Create Post

Creates a new blog post.

```rest
POST /posts
```

**Request:**

```json
{
  "title": "New Blog Post",
  "description": "Description of the new post",
  "content": "Full content of the blog post...",
  "imageUrl": "https://example.com/images/newpost.jpg",
  "tags": ["spring", "tutorial"]
}
```

**Response:**

```json
{
  "id": 43,
  "title": "New Blog Post",
  "slug": "new-blog-post",
  "description": "Description of the new post",
  "content": "Full content of the blog post...",
  "imageUrl": "https://example.com/images/newpost.jpg",
  "date": "2023-12-16T14:22:05",
  "author": "John Doe",
  "tags": ["spring", "tutorial"]
}
```

**Status Codes:**

- 201: Created
- 400: Bad Request (validation error)
- 401: Unauthorized
- 403: Forbidden (insufficient privileges)
- 500: Server Error

#### Update Post

Updates an existing blog post.

```rest
PUT /posts/{id}
```

**Path Parameters:**

- `id`: The ID of the post to update

**Request:**

```json
{
  "title": "Updated Blog Post Title",
  "description": "Updated description",
  "content": "Updated content...",
  "imageUrl": "https://example.com/images/updated.jpg",
  "tags": ["spring", "java", "updated"]
}
```

**Response:**

```json
{
  "id": 1,
  "title": "Updated Blog Post Title",
  "slug": "updated-blog-post-title",
  "description": "Updated description",
  "content": "Updated content...",
  "imageUrl": "https://example.com/images/updated.jpg",
  "date": "2023-12-15T10:30:00",
  "updatedAt": "2023-12-16T15:45:22",
  "author": "John Doe",
  "tags": ["spring", "java", "updated"]
}
```

**Status Codes:**

- 200: Success
- 400: Bad Request (validation error)
- 401: Unauthorized
- 403: Forbidden (insufficient privileges)
- 404: Post not found
- 500: Server Error

#### Delete Post

Deletes a blog post.

```rest
DELETE /posts/{id}
```

**Path Parameters:**

- `id`: The ID of the post to delete

**Response:**

- No content

**Status Codes:**

- 204: No Content (successful deletion)
- 401: Unauthorized
- 403: Forbidden (insufficient privileges)
- 404: Post not found
- 500: Server Error

### Comments

#### Get Comments for Post

Retrieves all comments for a specific blog post.

```rest
GET /posts/{postId}/comments
```

**Path Parameters:**

- `postId`: The ID of the post

**Response:**

```json
[
  {
    "id": 1,
    "postId": 1,
    "name": "Alex Johnson",
    "content": "Great article! I especially liked the part about component design patterns.",
    "date": "2023-12-10T14:23:01",
    "avatar": "https://i.pravatar.cc/150?img=1",
    "replies": [
      {
        "id": 3,
        "parentId": 1,
        "name": "Author",
        "content": "Thanks for your comment!",
        "date": "2023-12-10T16:45:12",
        "avatar": "https://i.pravatar.cc/150?img=3",
        "isAuthor": true
      }
    ]
  },
  {
    "id": 2,
    "postId": 1,
    "name": "Sarah Miller",
    "content": "Thanks for sharing this information. It's really helpful for my current project.",
    "date": "2023-12-09T09:45:22",
    "avatar": "https://i.pravatar.cc/150?img=2",
    "replies": []
  }
]
```

**Status Codes:**

- 200: Success
- 404: Post not found
- 500: Server Error

#### Add Comment

Adds a new comment to a blog post.

```rest
POST /posts/{postId}/comments
```

**Path Parameters:**

- `postId`: The ID of the post

**Request:**

```json
{
  "name": "John Smith",
  "email": "john.smith@example.com",
  "content": "This is a great article! Thanks for sharing."
}
```

**Response:**

```json
{
  "id": 10,
  "postId": 1,
  "name": "John Smith",
  "content": "This is a great article! Thanks for sharing.",
  "date": "2023-12-16T16:30:45",
  "avatar": "https://i.pravatar.cc/150?img=45"
}
```

**Status Codes:**

- 201: Created
- 400: Bad Request (validation error)
- 404: Post not found
- 500: Server Error

#### Add Reply to Comment

Adds a reply to an existing comment.

```rest
POST /comments/{commentId}/replies
```

**Path Parameters:**

- `commentId`: The ID of the parent comment

**Request:**

```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "content": "I agree with your point about Spring Boot."
}
```

**Response:**

```json
{
  "id": 11,
  "parentId": 10,
  "name": "Jane Doe",
  "content": "I agree with your point about Spring Boot.",
  "date": "2023-12-16T17:05:12",
  "avatar": "https://i.pravatar.cc/150?img=32"
}
```

**Status Codes:**

- 201: Created
- 400: Bad Request (validation error)
- 404: Comment not found
- 500: Server Error

### Categories

#### Get All Categories

Retrieves all blog categories with post counts.

```rest
GET /categories
```

**Response:**

```json
[
  {
    "name": "Technology",
    "slug": "technology",
    "count": 12,
    "color": "primary"
  },
  {
    "name": "Development",
    "slug": "development",
    "count": 8,
    "color": "secondary"
  },
  {
    "name": "Design",
    "slug": "design",
    "count": 5,
    "color": "accent"
  }
]
```

**Status Codes:**

- 200: Success
- 500: Server Error

### Tags

#### Get All Tags

Retrieves all tags used in blog posts.

```rest
GET /tags
```

**Response:**

```json
[
  {
    "name": "spring",
    "count": 15
  },
  {
    "name": "java",
    "count": 22
  },
  {
    "name": "web",
    "count": 8
  }
]
```

**Status Codes:**

- 200: Success
- 500: Server Error

### Search

#### Search Posts

Searches for blog posts based on a query string.

```rest
GET /search
```

**Query Parameters:**

- `q`: Search query
- `category` (optional): Filter by category
- `tag` (optional): Filter by tag
- `page` (optional): Page number (default: 0)
- `size` (optional): Number of items per page (default: 10)

**Response:**

```json
{
  "content": [
    {
      "id": 5,
      "title": "Introduction to Spring Boot",
      "slug": "introduction-to-spring-boot",
      "description": "Learn the basics of Spring Boot for building Java applications",
      "imageUrl": "https://example.com/images/spring-boot.jpg",
      "date": "2023-11-20T14:30:00",
      "author": "Jane Doe",
      "tags": ["spring", "java", "tutorial"]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

**Status Codes:**

- 200: Success
- 400: Bad Request (invalid query parameters)
- 500: Server Error

### User Authentication

#### Login

Authenticates a user and returns a JWT token.

```rest
POST /auth/login
```

**Request:**

```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 3600
}
```

**Status Codes:**

- 200: Success
- 401: Unauthorized (invalid credentials)
- 500: Server Error

#### Register

Registers a new user.

```rest
POST /auth/register
```

**Request:**

```json
{
  "username": "newuser",
  "password": "securepassword123",
  "email": "newuser@example.com",
  "name": "New User"
}
```

**Response:**

```json
{
  "id": 5,
  "username": "newuser",
  "email": "newuser@example.com",
  "name": "New User",
  "role": "USER",
  "createdAt": "2023-12-16T18:22:30"
}
```

**Status Codes:**

- 201: Created
- 400: Bad Request (validation error or username/email already exists)
- 500: Server Error

## Error Responses

All error responses follow this format:

```json
{
  "timestamp": "2023-12-16T19:30:45.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/posts",
  "details": [
    "Title is required",
    "Content must be at least 50 characters"
  ]
}
```

## Data Models

### Post

```json
{
  "id": 1,
  "title": "Sample Blog Post",
  "slug": "sample-blog-post",
  "description": "This is a short description of the blog post",
  "content": "Full content of the blog post...",
  "imageUrl": "https://example.com/images/sample.jpg",
  "date": "2023-12-15T10:30:00",
  "updatedAt": "2023-12-15T12:45:10",
  "author": "John Doe",
  "authorId": 1,
  "tags": ["spring", "java", "web"],
  "categoryId": 2,
  "categoryName": "Development",
  "published": true
}
```

### Comment

```json
{
  "id": 1,
  "postId": 1,
  "name": "Alex Johnson",
  "email": "alex@example.com",
  "content": "Great article!",
  "date": "2023-12-10T14:23:01",
  "avatar": "https://i.pravatar.cc/150?img=1",
  "parentId": null,
  "isAuthor": false,
  "replies": []
}
```

### Category

```json
{
  "id": 1,
  "name": "Technology",
  "slug": "technology",
  "description": "Posts about technology topics",
  "count": 12,
  "color": "primary"
}
```

### Tag

```json
{
  "id": 1,
  "name": "spring",
  "count": 15
}
```

### User

```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "name": "Admin User",
  "bio": "Blog administrator",
  "avatar": "https://example.com/avatars/admin.jpg",
  "role": "ADMIN",
  "createdAt": "2023-01-01T00:00:00",
  "lastLogin": "2023-12-16T09:30:45"
}
```

## Rate Limiting

The API implements rate limiting to prevent abuse:

- For anonymous users: 30 requests per minute
- For authenticated users: 60 requests per minute
- For admin users: 120 requests per minute

When the rate limit is exceeded, the API will return a 429 (Too Many Requests) status code.

## Versioning

This API follows semantic versioning. The current version is v1.

Future versions will be available at `/api/v2`, `/api/v3`, etc.
