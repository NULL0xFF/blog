<script lang="ts">
  import {goto} from '$app/navigation';
  import {page} from '$app/state';
  import {onMount} from 'svelte';
  import {getAllCategories} from '$lib/services/blogService';

  // Define types for our data
  interface Category {
    id: number;
    name: string;
    slug: string;
    description?: string;
  }

  // Search state
  let searchTerm = $state('');
  let isSearching = $state(false);

  // Categories for filter dropdown
  let categories = $state<{ id: number; name: string; slug: string }[]>([]);
  let selectedCategory = $state('');

  // Date filter options
  let dateFilter = $state('');
  const dateOptions = [
    {value: 'week', label: 'Last week'},
    {value: 'month', label: 'Last month'},
    {value: 'year', label: 'Last year'}
  ];

  // Sort options
  let sortOption = $state('recent');
  const sortOptions = [
    {value: 'recent', label: 'Most Recent'},
    {value: 'popular', label: 'Most Popular'},
    {value: 'asc', label: 'A-Z'},
    {value: 'desc', label: 'Z-A'}
  ];

  // Load categories on mount
  onMount(async () => {
    try {
      const categoriesData = await getAllCategories();
      categories = categoriesData.map((cat: Category) => ({
        id: cat.id,
        name: cat.name,
        slug: cat.slug
      }));
    } catch (err) {
      console.error('Failed to load categories:', err);
    }

    // Initialize search term from URL if it exists
    const urlSearchTerm = page.url.searchParams.get('q');
    if (urlSearchTerm) {
      searchTerm = urlSearchTerm;
    }

    // Initialize category from URL if it exists
    const urlCategory = page.url.searchParams.get('category');
    if (urlCategory) {
      selectedCategory = urlCategory;
    }

    // Initialize date filter from URL if it exists
    const urlDateFilter = page.url.searchParams.get('date');
    if (urlDateFilter) {
      dateFilter = urlDateFilter;
    }

    // Initialize sort option from URL if it exists
    const urlSortOption = page.url.searchParams.get('sort');
    if (urlSortOption) {
      sortOption = urlSortOption;
    }
  });

  function handleSearch(e: Event) {
    e.preventDefault();
    if (!searchTerm.trim() && !selectedCategory && !dateFilter && !sortOption) return;

    isSearching = true;

    // Build query parameters
    const queryParams = new URLSearchParams();

    if (searchTerm.trim()) {
      queryParams.set('q', searchTerm.trim());
    }

    if (selectedCategory) {
      queryParams.set('category', selectedCategory);
    }

    if (dateFilter) {
      queryParams.set('date', dateFilter);
    }

    if (sortOption) {
      queryParams.set('sort', sortOption);
    }

    // Reset to page 1 when searching
    queryParams.set('page', '1');

    // Navigate to search results page
    goto(`/blog?${queryParams.toString()}`);

    // This would be an actual API call in a real implementation
    setTimeout(() => {
      isSearching = false;
    }, 500);
  }

  function selectCategory(slug: string) {
    selectedCategory = selectedCategory === slug ? '' : slug;
  }

  function selectDateFilter(value: string) {
    dateFilter = dateFilter === value ? '' : value;
  }

  function selectSortOption(value: string) {
    sortOption = value;
  }
</script>

<div class="card bg-base-100 mb-8 shadow-lg">
  <div class="card-body p-4">
    <form class="w-full" onsubmit={handleSearch}>
      <div class="input-group w-full">
        <input
            bind:value={searchTerm}
            class="input input-bordered w-full"
            disabled={isSearching}
            placeholder="Search blog posts..."
            type="text"
        />
        <button class="btn btn-primary" disabled={isSearching} type="submit">
          {#if isSearching}
            <span class="loading loading-spinner loading-sm"></span>
          {:else}
            <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
            >
              <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
          {/if}
        </button>
      </div>

      <!-- Active filters display -->
      {#if selectedCategory || dateFilter || sortOption !== 'recent'}
        <div class="mt-2 flex flex-wrap gap-2">
          {#if selectedCategory}
            <div class="badge badge-primary gap-1">
              Category: {categories.find((c) => c.slug === selectedCategory)?.name ||
            selectedCategory}
              <button type="button" onclick={() => (selectedCategory = '')}>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-3 w-3"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          {/if}

          {#if dateFilter}
            <div class="badge badge-secondary gap-1">
              Date: {dateOptions.find((d) => d.value === dateFilter)?.label || dateFilter}
              <button type="button" onclick={() => (dateFilter = '')}>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-3 w-3"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          {/if}

          {#if sortOption !== 'recent'}
            <div class="badge badge-accent gap-1">
              Sort: {sortOptions.find((s) => s.value === sortOption)?.label || sortOption}
              <button type="button" onclick={() => (sortOption = 'recent')}>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-3 w-3"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          {/if}
        </div>
      {/if}

      <!-- Search filters -->
      <div class="mt-2 flex flex-wrap gap-2">
        <div class="dropdown dropdown-hover">
          <button class="btn btn-sm btn-ghost" type="button">
            Categories
            <svg
                class="h-4 w-4"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
            >
              <path
                  d="M19.5 8.25l-7.5 7.5-7.5-7.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
              />
            </svg>
          </button>
          <ul class="dropdown-content bg-base-100 rounded-box z-[1] w-52 p-2 shadow-md">
            {#if categories.length === 0}
              <li class="text-base-content/70 p-2 text-center">Loading categories...</li>
            {:else}
              {#each categories as category}
                <li>
                  <button
                      type="button"
                      class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
                      class:bg-base-200={selectedCategory === category.slug}
                      onclick={() => selectCategory(category.slug)}
                  >
                    {category.name}
                  </button>
                </li>
              {/each}
            {/if}
          </ul>
        </div>

        <div class="dropdown dropdown-hover">
          <button class="btn btn-sm btn-ghost" type="button">
            Date
            <svg
                class="h-4 w-4"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
            >
              <path
                  d="M19.5 8.25l-7.5 7.5-7.5-7.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
              />
            </svg>
          </button>
          <ul class="dropdown-content bg-base-100 rounded-box z-[1] w-52 p-2 shadow-md">
            {#each dateOptions as option}
              <li>
                <button
                    type="button"
                    class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
                    class:bg-base-200={dateFilter === option.value}
                    onclick={() => selectDateFilter(option.value)}
                >
                  {option.label}
                </button>
              </li>
            {/each}
          </ul>
        </div>

        <div class="dropdown dropdown-hover">
          <button class="btn btn-sm btn-ghost" type="button">
            Sort By
            <svg
                class="h-4 w-4"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
            >
              <path
                  d="M19.5 8.25l-7.5 7.5-7.5-7.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
              />
            </svg>
          </button>
          <ul class="dropdown-content bg-base-100 rounded-box z-[1] w-52 p-2 shadow-md">
            {#each sortOptions as option}
              <li>
                <button
                    type="button"
                    class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
                    class:bg-base-200={sortOption === option.value}
                    onclick={() => selectSortOption(option.value)}
                >
                  {option.label}
                </button>
              </li>
            {/each}
          </ul>
        </div>
      </div>
    </form>
  </div>
</div>
