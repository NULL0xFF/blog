import { getAllPosts } from '$lib/services/blogService';
import type { LayoutLoad } from './$types';
import type { Post } from '$lib/types/blog';
import { error } from '@sveltejs/kit';

export const load: LayoutLoad = async () => {
  try {
    // Get all posts and pick the first 3 for featured posts
    const allPosts = await getAllPosts();

    // Sort by date and get the most recent posts for featured section
    const featuredPosts = allPosts
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
      .slice(0, 3);

    return {
      featuredPosts
    };
  } catch (err) {
    // For layout errors, we'll still return an empty array rather than throwing
    // This allows pages to still render even if featured posts can't be loaded
    console.error('Failed to load featured posts:', err);

    return {
      featuredPosts: []
    };
  }
};