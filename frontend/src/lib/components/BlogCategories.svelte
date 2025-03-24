<script lang="ts">
  import {onMount} from 'svelte';
  import {getCategoriesWithPostCount} from '$lib/services/blogService';

  // Define type for category from API
  interface CategoryWithCount {
    id: number;
    name: string;
    slug: string;
    color?: string;
    postCount: number;
  }

  // State for categories
  let categories = $state<
      { id: number; name: string; slug: string; color: string; postCount: number }[]
  >([]);
  let isLoading = $state(true);
  let error = $state('');

  // Color classes for categories (rotate through these)
  const colorClasses = [
    'badge-primary',
    'badge-secondary',
    'badge-accent',
    'badge-info',
    'badge-success',
    'badge-warning'
  ];

  // Fetch categories on component mount
  onMount(async () => {
    try {
      const categoriesData = await getCategoriesWithPostCount();
      categories = categoriesData.map((category: CategoryWithCount, index: number) => ({
        id: category.id,
        name: category.name,
        slug: category.slug,
        postCount: category.postCount,
        color: category.color
            ? `badge-${category.color}`
            : colorClasses[index % colorClasses.length]
      }));
    } catch (err) {
      console.error('Failed to load categories:', err);
      error = 'Failed to load categories.';
    } finally {
      isLoading = false;
    }
  });
</script>

<div class="card bg-base-100 shadow-lg">
  <div class="card-body">
    <h3 class="card-title text-lg">Categories</h3>
    <div class="divider mt-0"></div>

    {#if isLoading}
      <div class="flex justify-center py-4">
        <span class="loading loading-spinner loading-md"></span>
      </div>
    {:else if error}
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
        <span>{error}</span>
      </div>
    {:else if categories.length === 0}
      <p class="text-base-content/70 py-4 text-center">No categories found.</p>
    {:else}
      <div class="flex flex-wrap gap-2">
        {#each categories as category}
          <a
              href={`/blog?category=${encodeURIComponent(category.slug)}`}
              class="badge {category.color} badge-lg hover:badge-ghost gap-1 transition-colors duration-300"
          >
            {category.name}
            <span class="badge badge-sm">{category.postCount}</span>
          </a>
        {/each}
      </div>
    {/if}
  </div>
</div>
