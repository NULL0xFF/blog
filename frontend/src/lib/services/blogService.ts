import {browser} from '$app/environment';
import type {PaginatedResponse, Post} from '$lib/types/blog';
import {error} from '@sveltejs/kit';

// Base API URL - in development, this will be the local server
// In production, it could be relative or the same domain
const API_URL = '/api';

/**
 * Normalizes post data from the backend API format to our frontend model
 */
export function normalizePost(post: any): Post {
  return {
    id: post.id,
    title: post.title,
    slug: post.slug,
    description: post.description || '',
    content: post.content || '',
    imageUrl: post.imageUrl || `https://picsum.photos/seed/${post.id}/800/600`,
    date: post.publishedAt || post.createdAt || new Date().toISOString(),
    author: post.author?.username || 'Anonymous',
    tags: post.tags?.map((tag: any) => tag.name) || []
  };
}

/**
 * Fetches all published blog posts
 */
export async function getAllPosts(): Promise<Post[]> {
  try {
    const response = await fetch(`${API_URL}/posts`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const data = await response.json();
    return (data.content || data).map(normalizePost);
  } catch (err) {
    console.error('Error fetching blog posts:', err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      return getMockPosts();
    }

    throw error(500, 'Failed to fetch blog posts');
  }
}

/**
 * Fetches paginated blog posts
 */
export async function getPaginatedPosts(page: number, limit: number): Promise<PaginatedResponse<Post>> {
  try {
    const response = await fetch(`${API_URL}/posts?page=${page - 1}&size=${limit}`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const data = await response.json();

    return {
      items: (data.content || []).map(normalizePost),
      currentPage: page,
      totalPages: data.totalPages || 1,
      totalItems: data.totalElements || data.content?.length || 0,
      itemsPerPage: limit
    };
  } catch (err) {
    console.error('Error fetching paginated posts:', err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      const mockData = getMockPaginatedPosts(page, limit);
      return mockData;
    }

    throw error(500, 'Failed to load blog posts');
  }
}

/**
 * Fetches a single blog post by slug
 */
export async function getPostBySlug(slug: string): Promise<Post> {
  try {
    const response = await fetch(`${API_URL}/posts/by-slug/${slug}`);

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

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      const mockPosts = getMockPosts();
      const mockPost = mockPosts.find(p => p.slug === slug);
      if (mockPost) return mockPost;
    }

    // If it's already a SvelteKit error, rethrow it
    if (err && typeof err === 'object' && 'status' in err) {
      throw err;
    }

    throw error(500, `Failed to load post "${slug}"`);
  }
}

/**
 * Fetches posts by category slug
 */
export async function getPostsByCategory(categorySlug: string, page: number = 0, limit: number = 10): Promise<PaginatedResponse<Post>> {
  try {
    const response = await fetch(`${API_URL}/posts/by-category/${categorySlug}?page=${page}&size=${limit}`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const data = await response.json();

    return {
      items: (data.content || []).map(normalizePost),
      currentPage: page + 1,
      totalPages: data.totalPages || 1,
      totalItems: data.totalElements || data.content?.length || 0,
      itemsPerPage: limit
    };
  } catch (err) {
    console.error(`Error fetching posts for category "${categorySlug}":`, err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      const mockData = getMockPaginatedPosts(page + 1, limit);
      return mockData;
    }

    throw error(500, `Failed to load posts for category "${categorySlug}"`);
  }
}

/**
 * Fetches posts by tag slug
 */
export async function getPostsByTag(tagSlug: string, page: number = 0, limit: number = 10): Promise<PaginatedResponse<Post>> {
  try {
    const response = await fetch(`${API_URL}/posts/by-tag/${tagSlug}?page=${page}&size=${limit}`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const data = await response.json();

    return {
      items: (data.content || []).map(normalizePost),
      currentPage: page + 1,
      totalPages: data.totalPages || 1,
      totalItems: data.totalElements || data.content?.length || 0,
      itemsPerPage: limit
    };
  } catch (err) {
    console.error(`Error fetching posts for tag "${tagSlug}":`, err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      const mockData = getMockPaginatedPosts(page + 1, limit);
      return mockData;
    }

    throw error(500, `Failed to load posts for tag "${tagSlug}"`);
  }
}

/**
 * Search posts by query
 */
export async function searchPosts(query: string, page: number = 0, limit: number = 10): Promise<PaginatedResponse<Post>> {
  try {
    const response = await fetch(`${API_URL}/posts/search?query=${encodeURIComponent(query)}&page=${page}&size=${limit}`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    const data = await response.json();

    return {
      items: (data.content || []).map(normalizePost),
      currentPage: page + 1,
      totalPages: data.totalPages || 1,
      totalItems: data.totalElements || data.content?.length || 0,
      itemsPerPage: limit
    };
  } catch (err) {
    console.error(`Error searching posts with query "${query}":`, err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      const mockData = getMockPaginatedPosts(page + 1, limit);
      return mockData;
    }

    throw error(500, `Failed to search posts with query "${query}"`);
  }
}

/**
 * Fetches all categories from the API
 */
export async function getAllCategories() {
  try {
    const response = await fetch(`${API_URL}/categories`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    return await response.json();
  } catch (err) {
    console.error('Error fetching categories:', err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      return getMockCategories();
    }

    throw error(500, 'Failed to fetch categories');
  }
}

/**
 * Fetches all categories with post counts
 */
export async function getCategoriesWithPostCount() {
  try {
    const response = await fetch(`${API_URL}/categories/with-post-count`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    return await response.json();
  } catch (err) {
    console.error('Error fetching categories with post count:', err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      return getMockCategories();
    }

    throw error(500, 'Failed to fetch categories with post count');
  }
}

/**
 * Fetches all tags from the API
 */
export async function getAllTags() {
  try {
    const response = await fetch(`${API_URL}/tags`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    return await response.json();
  } catch (err) {
    console.error('Error fetching tags:', err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      return getMockTags();
    }

    throw error(500, 'Failed to fetch tags');
  }
}

/**
 * Fetches popular tags from the API
 */
export async function getPopularTags(limit: number = 10) {
  try {
    const response = await fetch(`${API_URL}/tags/popular?limit=${limit}`);

    if (!response.ok) {
      throw error(response.status, `API request failed with status ${response.status}`);
    }

    return await response.json();
  } catch (err) {
    console.error('Error fetching popular tags:', err);

    // For development, return mock data if the API isn't available
    if (browser && (err as any)?.status === 404) {
      return getMockTags().slice(0, limit);
    }

    throw error(500, 'Failed to fetch popular tags');
  }
}

// ======= MOCK DATA FOR DEVELOPMENT =======
// These functions provide mock data when the API isn't available

function getMockPosts(): Post[] {
  return [
    {
      id: 1,
      title: 'Getting Started with Svelte',
      slug: 'getting-started-with-svelte',
      description: 'Learn the basics of Svelte and how to build your first application',
      content: '<p>Svelte is a radical new approach to building user interfaces. Whereas traditional frameworks like React and Vue do the bulk of their work in the browser, Svelte shifts that work into a compile step that happens when you build your app.</p><p>Instead of using techniques like virtual DOM diffing, Svelte writes code that surgically updates the DOM when the state of your app changes.</p>',
      imageUrl: 'https://picsum.photos/seed/svelte/800/600',
      date: '2023-03-15T10:00:00Z',
      author: 'John Doe',
      tags: ['Svelte', 'JavaScript', 'Frontend']
    },
    {
      id: 2,
      title: 'Introduction to SvelteKit',
      slug: 'introduction-to-sveltekit',
      description: 'Discover the features of SvelteKit and how it makes building full-stack applications easier',
      content: '<p>SvelteKit is a framework for building web applications of all sizes, with a beautiful development experience and flexible filesystem-based routing.</p><p>Unlike single-page apps, SvelteKit doesn\'t compromise on SEO, progressive enhancement or the initial load experience — but unlike traditional server-rendered apps, navigation is instantaneous for that app-like feel.</p>',
      imageUrl: 'https://picsum.photos/seed/sveltekit/800/600',
      date: '2023-04-20T14:30:00Z',
      author: 'Jane Smith',
      tags: ['SvelteKit', 'Web Development', 'Full-stack']
    },
    {
      id: 3,
      title: 'Styling with Tailwind CSS',
      slug: 'styling-with-tailwind-css',
      description: 'Learn how to use Tailwind CSS to style your web applications quickly and efficiently',
      content: '<p>Tailwind CSS is a utility-first CSS framework packed with classes like flex, pt-4, text-center and rotate-90 that can be composed to build any design, directly in your markup.</p><p>It\'s designed to be extensible and customizable, and it\'s lightning fast thanks to its just-in-time compiler.</p>',
      imageUrl: 'https://picsum.photos/seed/tailwind/800/600',
      date: '2023-05-10T09:15:00Z',
      author: 'Alex Johnson',
      tags: ['CSS', 'Tailwind', 'Design']
    },
    {
      id: 4,
      title: 'Building Web Components with Svelte',
      slug: 'building-web-components-with-svelte',
      description: 'Create reusable web components using Svelte\'s custom element feature',
      content: '<p>Svelte components can be compiled to custom elements, also known as web components, which allows them to be used in any HTML environment.</p><p>This makes Svelte perfect for building shareable UI components that can be used across different frameworks or in traditional HTML/JavaScript applications.</p>',
      imageUrl: 'https://picsum.photos/seed/webcomponents/800/600',
      date: '2023-06-05T16:45:00Z',
      author: 'Maria Garcia',
      tags: ['Svelte', 'Web Components', 'JavaScript']
    },
    {
      id: 5,
      title: 'Data Visualization with D3 and Svelte',
      slug: 'data-visualization-with-d3-and-svelte',
      description: 'Combine the power of D3.js and Svelte to create stunning data visualizations',
      content: '<p>D3.js is a powerful library for data visualization, and when paired with Svelte\'s reactivity and component model, it can make complex visualizations more maintainable and easier to develop.</p><p>In this post, we\'ll explore how to integrate D3.js with Svelte to create responsive and interactive data visualizations.</p>',
      imageUrl: 'https://picsum.photos/seed/dataviz/800/600',
      date: '2023-07-12T11:20:00Z',
      author: 'David Kim',
      tags: ['D3.js', 'Data Visualization', 'Svelte']
    },
    {
      id: 6,
      title: 'Authentication in SvelteKit',
      slug: 'authentication-in-sveltekit',
      description: 'Implement user authentication in your SvelteKit applications',
      content: '<p>Authentication is a crucial part of many web applications. SvelteKit provides several ways to implement authentication, from traditional cookie-based auth to JWT tokens and OAuth providers.</p><p>This post covers the best practices for implementing secure authentication in your SvelteKit applications.</p>',
      imageUrl: 'https://picsum.photos/seed/auth/800/600',
      date: '2023-08-18T13:40:00Z',
      author: 'Sarah Wilson',
      tags: ['SvelteKit', 'Authentication', 'Security']
    }
  ];
}

function getMockPaginatedPosts(page: number, limit: number): PaginatedResponse<Post> {
  const mockPosts = getMockPosts();
  const startIndex = (page - 1) * limit;
  const endIndex = startIndex + limit;
  const paginatedItems = mockPosts.slice(startIndex, endIndex);

  return {
    items: paginatedItems,
    currentPage: page,
    totalPages: Math.ceil(mockPosts.length / limit),
    totalItems: mockPosts.length,
    itemsPerPage: limit
  };
}

function getMockCategories() {
  return [
    {id: 1, name: 'Technology', slug: 'technology', postCount: 12},
    {id: 2, name: 'Development', slug: 'development', postCount: 8},
    {id: 3, name: 'Design', slug: 'design', postCount: 5},
    {id: 4, name: 'Business', slug: 'business', postCount: 3},
    {id: 5, name: 'Marketing', slug: 'marketing', postCount: 4},
    {id: 6, name: 'Tutorials', slug: 'tutorials', postCount: 7}
  ];
}

function getMockTags() {
  return [
    {id: 1, name: 'Svelte', slug: 'svelte', postCount: 3},
    {id: 2, name: 'JavaScript', slug: 'javascript', postCount: 5},
    {id: 3, name: 'TypeScript', slug: 'typescript', postCount: 2},
    {id: 4, name: 'Web', slug: 'web', postCount: 6},
    {id: 5, name: 'Development', slug: 'development', postCount: 4},
    {id: 6, name: 'DaisyUI', slug: 'daisyui', postCount: 2},
    {id: 7, name: 'Tailwind', slug: 'tailwind', postCount: 3},
    {id: 8, name: 'CSS', slug: 'css', postCount: 4},
    {id: 9, name: 'HTML', slug: 'html', postCount: 2},
    {id: 10, name: 'API', slug: 'api', postCount: 1},
    {id: 11, name: 'Design', slug: 'design', postCount: 3}
  ];
}