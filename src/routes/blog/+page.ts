import type { PageLoad } from './$types';
import { getPaginatedPosts } from '$lib/services/blogService';
import type { BlogPageData } from '$lib/types/blog';
import { error } from '@sveltejs/kit';

export const load: PageLoad<BlogPageData> = async ({ url }) => {
  try {
    // Get the current page from the URL query parameters (default to page 1)
    const currentPage = parseInt(url.searchParams.get('page') || '1');
    const postsPerPage = 10;

    // Get paginated posts - this will throw an error if the API fails
    const paginatedResponse = await getPaginatedPosts(currentPage, postsPerPage);

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