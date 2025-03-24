<script lang="ts">
  import {onMount} from 'svelte';
  import BlogCategories from './BlogCategories.svelte';
  import type {Post} from '$lib/types/blog';
  import {getPopularTags} from '$lib/services/blogService';

  // Define types for tags from API
  interface TagWithCount {
    id: number;
    name: string;
    slug: string;
    postCount?: number;
  }

  let {popularPosts = []} = $props<{
    popularPosts?: Post[];
  }>();

  // Tags state
  let tags = $state<{ id: number; name: string; slug: string; postCount: number }[]>([]);
  let isLoadingTags = $state(true);
  let tagsError = $state('');

  // Archives - could be fetched from API in a real implementation
  const archives = [
    {month: '2023-12', label: 'December 2023'},
    {month: '2023-11', label: 'November 2023'},
    {month: '2023-10', label: 'October 2023'},
    {month: '2023-09', label: 'September 2023'},
    {month: '2023-08', label: 'August 2023'}
  ];

  onMount(async () => {
    try {
      const tagsData = await getPopularTags(15); // Get top 15 tags
      tags = tagsData.map((tag: TagWithCount) => ({
        id: tag.id,
        name: tag.name,
        slug: tag.slug,
        postCount: tag.postCount || 0
      }));
    } catch (err) {
      console.error('Failed to load tags:', err);
      tagsError = 'Failed to load tags.';
    } finally {
      isLoadingTags = false;
    }
  });
</script>

<div class="flex flex-col gap-6">
  <!-- Categories section -->
  <BlogCategories/>

  <!-- Popular posts section -->
  <div class="card bg-base-100 shadow-lg">
    <div class="card-body">
      <h3 class="card-title text-lg">Popular Posts</h3>
      <div class="divider mt-0"></div>

      {#if popularPosts.length > 0}
        <ul class="space-y-4">
          {#each popularPosts as post}
            <li
                class="hover:bg-base-200 flex items-start gap-3 rounded-lg p-2 transition-colors duration-300"
            >
              {#if post.imageUrl}
                <img
                    src={post.imageUrl}
                    alt={post.title}
                    class="h-16 w-16 rounded-lg object-cover"
                />
              {/if}
              <div>
                <a href={`/blog/${post.slug}`} class="hover:text-primary font-medium">
                  {post.title}
                </a>
                <p class="text-base-content/70 text-xs">
                  {new Date(post.date).toLocaleDateString()}
                </p>
              </div>
            </li>
          {/each}
        </ul>
      {:else}
        <div class="text-base-content/70 py-4 text-center">
          <p>No popular posts to display</p>
        </div>
      {/if}
    </div>
  </div>

  <!-- Tags cloud -->
  <div class="card bg-base-100 shadow-lg">
    <div class="card-body">
      <h3 class="card-title text-lg">Tags</h3>
      <div class="divider mt-0"></div>

      {#if isLoadingTags}
        <div class="flex justify-center py-4">
          <span class="loading loading-spinner loading-md"></span>
        </div>
      {:else if tagsError}
        <div class="alert alert-error">
          <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-6 w-6 shrink-0 stroke-current"
              fill="none"
              viewBox="0 0 24 24"
          >
            <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          <span>{tagsError}</span>
        </div>
      {:else if tags.length === 0}
        <p class="text-base-content/70 py-4 text-center">No tags found.</p>
      {:else}
        <div class="flex flex-wrap gap-2">
          {#each tags as tag}
            <a
                href={`/blog?tag=${tag.slug}`}
                class="badge badge-outline hover:badge-primary transition-colors duration-300"
                title="{tag.postCount} posts"
            >
              {tag.name}
            </a>
          {/each}
        </div>
      {/if}
    </div>
  </div>

  <!-- Archive section -->
  <div class="card bg-base-100 shadow-lg">
    <div class="card-body">
      <h3 class="card-title text-lg">Archives</h3>
      <div class="divider mt-0"></div>

      <ul class="menu menu-sm p-0">
        {#each archives as archive}
          <li>
            <a href={`/blog?month=${archive.month}`}>{archive.label}</a>
          </li>
        {/each}
      </ul>
    </div>
  </div>
</div>
