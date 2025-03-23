import type { PageLoad } from './$types';
import { getAllPosts } from '$lib/services/blogService';
import type { Post } from '$lib/types/blog';
import { error } from '@sveltejs/kit';

export const load: PageLoad = async (): Promise<{ posts: Post[] }> => {
  try {
    // This will throw an error if the API fails
    const posts = await getAllPosts();

    return {
      posts: posts.slice(0, 6) // Get just the first 6 posts for the homepage
    };
  } catch (err) {
    // If it's already a SvelteKit error, rethrow it
    if (err && typeof err === 'object' && 'status' in err) {
      throw err;
    }

    throw error(500, 'Failed to load blog posts');
  }
};