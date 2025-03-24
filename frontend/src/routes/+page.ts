import type {PageLoad} from './$types';
import {getAllPosts} from '$lib/services/blogService';
import type {Post} from '$lib/types/blog';
import {error} from '@sveltejs/kit';

export const load: PageLoad = async (): Promise<{ posts: Post[] }> => {
  try {
    // Fetch all posts and take the most recent ones for the homepage
    const posts = await getAllPosts();

    // Sort by date (most recent first) and take the first 6
    const sortedPosts = posts
    .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
    .slice(0, 6);

    return {
      posts: sortedPosts
    };
  } catch (err) {
    // If it's already a SvelteKit error, rethrow it
    if (err && typeof err === 'object' && 'status' in err) {
      throw err;
    }

    throw error(500, 'Failed to load blog posts');
  }
};