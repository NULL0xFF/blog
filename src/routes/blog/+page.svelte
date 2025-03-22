<script lang="ts">
	import type { PageData } from './$types';
	import PostCard from '$lib/components/PostCard.svelte';
	import Pagination from '$lib/components/Pagination.svelte';
	import BlogSearch from '$lib/components/BlogSearch.svelte';
	import BlogSidebar from '$lib/components/BlogSidebar.svelte';

	let { data } = $props<{ data: PageData }>();

	// Get popular posts for the sidebar (for demo purposes, we'll use the first 3)
	let sidebarPosts = data.posts.slice(0, 3);
</script>

<div class="py-8">
	<div class="mb-8 text-center">
		<h1 class="text-4xl font-bold">Blog Posts</h1>
		<p class="text-base-content/70 mt-2">Discover our latest articles and insights</p>
	</div>

	<!-- Main content with sidebar -->
	<div class="mb-12 flex flex-col gap-8 lg:flex-row">
		<!-- Main content area -->
		<div class="w-full lg:w-3/4">
			<!-- Search component -->
			<BlogSearch />

			<!-- Posts grid -->
			<div class="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-3">
				{#each data.posts as post}
					<PostCard {post} />
				{/each}
			</div>

			<!-- Show message when no posts are available -->
			{#if data.posts.length === 0}
				<div class="alert alert-info my-8 shadow-lg">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						fill="none"
						viewBox="0 0 24 24"
						class="h-6 w-6 shrink-0 stroke-current"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
						></path>
					</svg>
					<span>No posts found. Please check back later!</span>
				</div>
			{/if}

			<!-- Pagination component -->
			<Pagination currentPage={data.currentPage} totalPages={data.totalPages} />
		</div>

		<!-- Sidebar -->
		<div class="w-full lg:w-1/4">
			<BlogSidebar popularPosts={sidebarPosts} />
		</div>
	</div>
</div>
