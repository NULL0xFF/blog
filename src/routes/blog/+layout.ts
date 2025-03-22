import { getAllPosts } from '$lib/services/blogService';
import type { LayoutLoad } from './$types';
import type { Post } from '$lib/types/blog';

export const load: LayoutLoad = async () => {
  // Get all posts and pick the first 3 for featured posts
  const allPosts = await getAllPosts();

  // Simulate featuring the most recent posts (in a real app you might have a featured flag)
  const featuredPosts = allPosts
    .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
    .slice(0, 3);

  return {
    featuredPosts
  };
};