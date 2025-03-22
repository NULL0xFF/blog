import type { PageLoad } from './$types';
import { getPostBySlug, getAllPosts } from '$lib/services/blogService';
import { error } from '@sveltejs/kit';
import type { PostPageData } from '$lib/types/blog';

export const load: PageLoad<PostPageData> = async ({ params, parent }) => {
  try {
    // Get featured posts from parent layout data
    const { featuredPosts } = await parent();

    // Get the specific post
    const post = await getPostBySlug(params.slug);

    if (!post) {
      throw error(404, `Post with slug "${params.slug}" not found`);
    }

    return {
      post,
      featuredPosts
    };
  } catch (e) {
    // If error is already a SvelteKit error with status, rethrow it
    if (e && typeof e === 'object' && 'status' in e) {
      throw e;
    }

    // Otherwise throw a 404
    throw error(404, `Post with slug "${params.slug}" not found`);
  }
};