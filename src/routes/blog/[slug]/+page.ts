import type { PageLoad } from './$types';
import { getPostBySlug, getAllPosts } from '$lib/services/blogService';
import { error } from '@sveltejs/kit';
import type { PostPageData } from '$lib/types/blog';

export const load: PageLoad<PostPageData> = async ({ params, parent }) => {
  try {
    // Get featured posts from parent layout data
    const { featuredPosts } = await parent();

    // Get the specific post - this will throw an error if the API fails
    const post = await getPostBySlug(params.slug);

    return {
      post,
      featuredPosts
    };
  } catch (err) {
    // If it's already a SvelteKit error, rethrow it
    if (err && typeof err === 'object' && 'status' in err) {
      throw err;
    }

    throw error(500, `Failed to load post "${params.slug}"`);
  }
};