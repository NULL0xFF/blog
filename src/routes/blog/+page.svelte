<script lang="ts">
	import type { PageData } from './$types';
	import PostCard from '$lib/components/PostCard.svelte';
	import Pagination from '$lib/components/Pagination.svelte';
	import BlogSearch from '$lib/components/BlogSearch.svelte';
	import BlogSidebar from '$lib/components/BlogSidebar.svelte';
	import BackToTopButton from '$lib/components/BackToTopButton.svelte';

	let { data } = $props<{ data: PageData }>();

	// Get popular posts for the sidebar (for demo purposes, we'll use the first 3)
	// If posts are empty, sidebar will just show empty arrays
	let sidebarPosts = data.posts?.slice(0, 3) || [];
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

			{#if data.posts && data.posts.length > 0}
				<!-- Posts grid -->
				<div class="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-3">
					{#each data.posts as post}
						<PostCard {post} />
					{/each}
				</div>

				<!-- Pagination component -->
				<Pagination currentPage={data.currentPage} totalPages={data.totalPages} />
			{:else}
				<!-- Empty state for no posts -->
				<div class="card bg-base-100 py-16 shadow-lg">
					<div class="card-body text-center">
						<svg
							xmlns="http://www.w3.org/2000/svg"
							fill="none"
							viewBox="0 0 24 24"
							stroke-width="1.5"
							stroke="currentColor"
							class="text-base-content/50 mx-auto mb-4 h-16 w-16"
						>
							<path
								stroke-linecap="round"
								stroke-linejoin="round"
								d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z"
							/>
						</svg>
						<h2 class="card-title justify-center text-2xl">No Posts Found</h2>
						<p class="text-base-content/70 mt-2">
							We couldn't find any blog posts. Please check back later!
						</p>
						<div class="card-actions mt-6 justify-center">
							<button class="btn btn-outline" onclick={() => window.location.reload()}>
								<svg
									xmlns="http://www.w3.org/2000/svg"
									fill="none"
									viewBox="0 0 24 24"
									stroke-width="1.5"
									stroke="currentColor"
									class="mr-2 h-5 w-5"
								>
									<path
										stroke-linecap="round"
										stroke-linejoin="round"
										d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.99"
									/>
								</svg>
								Refresh
							</button>
							<a href="/" class="btn btn-primary">
								<svg
									xmlns="http://www.w3.org/2000/svg"
									fill="none"
									viewBox="0 0 24 24"
									stroke-width="1.5"
									stroke="currentColor"
									class="mr-2 h-5 w-5"
								>
									<path
										stroke-linecap="round"
										stroke-linejoin="round"
										d="M2.25 12l8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
									/>
								</svg>
								Go Home
							</a>
						</div>
					</div>
				</div>
			{/if}
		</div>

		<!-- Sidebar -->
		<div class="w-full lg:w-1/4">
			<BlogSidebar popularPosts={sidebarPosts} />
		</div>
	</div>
</div>

<!-- Back to top button -->
<BackToTopButton />
