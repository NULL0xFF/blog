import { browser } from '$app/environment';
import type { Post, PaginatedResponse } from '$lib/types/blog';
import { error } from '@sveltejs/kit';

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
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const posts = await response.json();
    return posts.map(normalizePost);
  } catch (err) {
    console.error('Error fetching blog posts:', err);
    throw error(500, 'Failed to fetch blog posts');
  }
}

/**
 * Fetches paginated blog posts
 */
export async function getPaginatedPosts(page: number, limit: number): Promise<PaginatedResponse<Post>> {
  try {
    // In a real API, you would call an endpoint with pagination parameters
    const response = await fetch(`${API_URL}/?page=${page}&limit=${limit}`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const data = await response.json();

    return {
      items: data.items.map(normalizePost),
      currentPage: data.currentPage || page,
      totalPages: data.totalPages || 1,
      totalItems: data.totalItems || data.items.length,
      itemsPerPage: data.itemsPerPage || limit
    };
  } catch (err) {
    console.error('Error fetching paginated posts:', err);
    throw error(500, 'Failed to load blog posts');
  }
}

/**
 * Fetches a single blog post by slug
 */
export async function getPostBySlug(slug: string): Promise<Post> {
  try {
    const response = await fetch(`${API_URL}/posts/${slug}`);

    if (!response.ok) {
      if (response.status === 404) {
        throw error(404, `Post with slug "${slug}" not found`);
      }
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const post = await response.json();
    return normalizePost(post);
  } catch (err) {
    console.error(`Error fetching post with slug "${slug}":`, err);

    // If it's already a SvelteKit error, rethrow it
    if (err && typeof err === 'object' && 'status' in err) {
      throw err;
    }

    throw error(500, `Failed to load post "${slug}"`);
  }
}