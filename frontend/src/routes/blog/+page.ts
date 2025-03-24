import type {PageLoad} from './$types';
import {
  getPaginatedPosts,
  getPostsByCategory,
  getPostsByTag,
  searchPosts
} from '$lib/services/blogService';
import type {BlogPageData} from '$lib/types/blog';
import {error} from '@sveltejs/kit';

export const load: PageLoad<BlogPageData> = async ({url}) => {
  try {
    // Get the current page from the URL query parameters (default to page 1)
    const currentPage = parseInt(url.searchParams.get('page') || '1');
    const postsPerPage = 10; // Default posts per page

    // Get other filter parameters
    const query = url.searchParams.get('q');
    const categorySlug = url.searchParams.get('category');
    const tagSlug = url.searchParams.get('tag');
    const sortBy = url.searchParams.get('sort') || 'recent';

    let paginatedResponse;

    // Determine which API endpoint to use based on query parameters
    if (query) {
      // Search posts by query
      paginatedResponse = await searchPosts(query, currentPage - 1, postsPerPage);
    } else if (categorySlug) {
      // Get posts by category
      paginatedResponse = await getPostsByCategory(categorySlug, currentPage - 1, postsPerPage);
    } else if (tagSlug) {
      // Get posts by tag
      paginatedResponse = await getPostsByTag(tagSlug, currentPage - 1, postsPerPage);
    } else {
      // Get all posts (paginated)
      paginatedResponse = await getPaginatedPosts(currentPage, postsPerPage);
    }

    return {
      posts: paginatedResponse.items,
      currentPage: paginatedResponse.currentPage,
      totalPages: paginatedResponse.totalPages,
      postsPerPage: paginatedResponse.itemsPerPage
    };
  } catch (err) {
    // If it's already a SvelteKit error, rethrow it
    if (err && typeof err === 'object' && 'status' in err) {
      throw err;
    }

    throw error(500, 'Failed to load blog posts');
  }
};