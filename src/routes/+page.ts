import type { PageLoad } from './$types';
import type { Post } from '$lib/types/blog';

export const load: PageLoad = async ({ fetch }): Promise<{ posts: Post[] }> => {
  try {
    // Fetch blog posts from the API
    const response = await fetch('http://localhost/api/');

    if (!response.ok) {
      throw new Error(`API request failed with status ${response.status}`);
    }

    const rawPosts = await response.json();

    // Transform the raw posts into our Post type format
    return {
      posts: rawPosts.map((post: any): Post => ({
        id: post.id,
        title: post.title,
        slug: post.slug || post.id.toString(),
        description: post.description || post.excerpt || post.summary || '',
        content: post.content || '',
        imageUrl: post.imageUrl || post.featuredImage || post.thumbnail || `https://picsum.photos/seed/${post.id}/800/600`,
        date: post.publishedAt || post.date || new Date().toISOString(),
        author: post.author?.name || post.authorName || undefined,
        tags: post.tags || []
      }))
    };
  } catch (error) {
    console.error('Error fetching blog posts:', error);

    // Return some fallback data in case of an error
    return {
      posts: [
        {
          id: 1,
          title: "Getting Started with SvelteKit",
          slug: "getting-started-with-sveltekit",
          description: "Learn how to build modern web applications with SvelteKit and DaisyUI.",
          content: "This is the full content of the article about getting started with SvelteKit.",
          imageUrl: "https://picsum.photos/seed/svelte1/800/600",
          date: "2023-09-15",
          author: "John Doe",
          tags: ["svelte", "sveltekit", "tutorial"]
        },
        {
          id: 2,
          title: "Building a Blog with SvelteKit",
          slug: "building-a-blog-with-sveltekit",
          description: "A step-by-step guide to creating your own blog using SvelteKit and a REST API.",
          content: "This is the full content of the article about building a blog with SvelteKit.",
          imageUrl: "https://picsum.photos/seed/svelte2/800/600",
          date: "2023-09-22",
          author: "Jane Smith",
          tags: ["svelte", "blog", "api"]
        },
        {
          id: 3,
          title: "Styling Your SvelteKit App with DaisyUI",
          slug: "styling-with-daisyui",
          description: "Learn how to use DaisyUI to create beautiful user interfaces in your SvelteKit applications.",
          content: "This is the full content of the article about styling with DaisyUI.",
          imageUrl: "https://picsum.photos/seed/svelte3/800/600",
          date: "2023-10-05",
          author: "Alex Johnson",
          tags: ["daisyui", "styling", "css"]
        }
      ]
    };
  }
};