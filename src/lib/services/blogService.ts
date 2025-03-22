import { browser } from '$app/environment';
import type { Post, PaginatedResponse } from '$lib/types/blog';

const API_URL = 'http://localhost/api';

/**
 * Normalizes post data from various API formats
 */
export function normalizePost(post: any): Post {
  return {
    id: post.id,
    title: post.title,
    slug: post.slug || post.id.toString(),
    description: post.description || post.excerpt || post.summary || '',
    content: post.content || '',
    imageUrl: post.imageUrl || post.featuredImage || post.thumbnail || `https://picsum.photos/seed/${post.id}/800/600`,
    date: post.publishedAt || post.date || new Date().toISOString(),
    author: post.author?.name || post.authorName || 'Anonymous',
    tags: post.tags || []
  };
}

/**
 * Fetches all blog posts from the API
 */
export async function getAllPosts(): Promise<Post[]> {
  try {
    const response = await fetch(`${API_URL}/`);

    if (!response.ok) {
      throw new Error(`API request failed with status ${response.status}`);
    }

    const posts = await response.json();
    return posts.map(normalizePost);
  } catch (error) {
    console.error('Error fetching blog posts:', error);
    return generateMockPosts(30);
  }
}

/**
 * Fetches paginated blog posts
 */
export async function getPaginatedPosts(page: number, limit: number): Promise<PaginatedResponse<Post>> {
  try {
    // In a real API, you would call an endpoint with pagination parameters
    // const response = await fetch(`${API_URL}/?page=${page}&limit=${limit}`);

    // For this implementation, we'll fetch all posts and manually paginate
    const allPosts = await getAllPosts();

    const startIndex = (page - 1) * limit;
    const endIndex = startIndex + limit;
    const paginatedPosts = allPosts.slice(startIndex, endIndex);

    return {
      items: paginatedPosts,
      currentPage: page,
      totalPages: Math.ceil(allPosts.length / limit),
      totalItems: allPosts.length,
      itemsPerPage: limit
    };
  } catch (error) {
    console.error('Error fetching paginated posts:', error);

    // Generate some mock data
    const mockPosts = generateMockPosts(30);
    const startIndex = (page - 1) * limit;
    const endIndex = startIndex + limit;

    return {
      items: mockPosts.slice(startIndex, endIndex),
      currentPage: page,
      totalPages: Math.ceil(mockPosts.length / limit),
      totalItems: mockPosts.length,
      itemsPerPage: limit
    };
  }
}

/**
 * Fetches a single blog post by slug
 */
export async function getPostBySlug(slug: string): Promise<Post> {
  try {
    // In a real API, you would have a dedicated endpoint for fetching a post by slug
    // const response = await fetch(`${API_URL}/posts/${slug}`);

    // For this implementation, we'll fetch all posts and find the one with the matching slug
    const allPosts = await getAllPosts();
    const post = allPosts.find(post => post.slug === slug);

    if (!post) {
      throw new Error(`Post with slug "${slug}" not found`);
    }

    return post;
  } catch (error) {
    console.error(`Error fetching post with slug "${slug}":`, error);

    // Return a mock post
    return generateMockPost(slug);
  }
}

/**
 * Generates mock posts for fallback content
 */
export function generateMockPosts(count: number): Post[] {
  return Array.from({ length: count }, (_, i) => ({
    id: i + 1,
    title: `Blog Post ${i + 1}`,
    slug: `blog-post-${i + 1}`,
    description: `This is a description for blog post ${i + 1}. This is mock data shown because the API request failed.`,
    content: generateMockContent(i + 1),
    imageUrl: `https://picsum.photos/seed/blog${i + 1}/800/600`,
    date: new Date(Date.now() - i * 86400000).toISOString(),
    author: 'Mock Author',
    tags: ['mock', 'sample', i % 2 === 0 ? 'even' : 'odd']
  }));
}

/**
 * Generates a single mock post by slug
 */
export function generateMockPost(slug: string): Post {
  return {
    id: slug,
    title: `Blog Post: ${slug}`,
    slug: slug,
    description: "This is a fallback description because the API request failed.",
    content: generateMockContent(slug),
    imageUrl: `https://picsum.photos/seed/${slug}/800/600`,
    date: new Date().toISOString(),
    author: 'Mock Author',
    tags: ['mock', 'fallback']
  };
}

/**
 * Generates mock content for a post
 */
function generateMockContent(seed: string | number): string {
  return `
    <p>This is the full content of "Blog Post ${seed}". This is mock data shown because the API request failed.</p>
    
    <h2>Introduction</h2>
    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam euismod, nisl eget ultricies ultrices, 
    nisl nisl aliquam nisl, eget ultricies nisl nisl eget nisl.</p>
    
    <h2>Main Section</h2>
    <p>Praesent eget sem vel leo ultrices bibendum. Aenean faucibus. Morbi dolor nulla, malesuada eu, 
    pulvinar at, mollis ac, nulla. Curabitur auctor semper nulla. Donec varius orci eget risus.</p>
    
    <h2>Conclusion</h2>
    <p>Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa. 
    Donec dapibus. Duis at velit eu est congue elementum.</p>
  `;
}