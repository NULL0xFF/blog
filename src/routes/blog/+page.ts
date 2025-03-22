import type { PageLoad } from './$types';
import { getPaginatedPosts } from '$lib/services/blogService';
import type { BlogPageData } from '$lib/types/blog';

export const load: PageLoad<BlogPageData> = async ({ fetch, url }) => {
  // Get the current page from the URL query parameters (default to page 1)
  const currentPage = parseInt(url.searchParams.get('page') || '1');
  const postsPerPage = 10;

  // Get paginated posts
  const paginatedResponse = await getPaginatedPosts(currentPage, postsPerPage);

  return {
    posts: paginatedResponse.items,
    currentPage: paginatedResponse.currentPage,
    totalPages: paginatedResponse.totalPages,
    postsPerPage: paginatedResponse.itemsPerPage
  };
};