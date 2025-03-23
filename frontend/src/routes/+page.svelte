<script lang="ts">
	import BackToTopButton from '$lib/components/BackToTopButton.svelte';

	// The error was related to passing a type argument to $props()
	// In Svelte 5, we need to specify the type differently
	let { data } = $props();
</script>

<div class="container mx-auto py-8">
	<h1 class="mb-8 text-center text-3xl font-bold">Latest Blog Posts</h1>

	{#if data.posts && data.posts.length > 0}
		<!-- Blog posts grid -->
		<div class="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
			{#each data.posts as post}
				<div class="card bg-base-100 shadow-xl">
					{#if post.imageUrl}
						<figure>
							<img src={post.imageUrl} alt={post.title} class="h-48 w-full object-cover" />
						</figure>
					{/if}

					<div class="card-body">
						<h2 class="card-title">{post.title}</h2>
						<p>{post.description}</p>
						<div class="card-actions mt-4 justify-end">
							<a href={`/blog/${post.slug}`} class="btn btn-primary">Read More</a>
						</div>
					</div>
				</div>
			{/each}
		</div>
	{:else}
		<!-- Empty state -->
		<div class="hero bg-base-200 min-h-[400px] rounded-lg shadow-lg">
			<div class="hero-content text-center">
				<div class="max-w-md">
					<h1 class="text-3xl font-bold">Welcome to My Blog</h1>
					<p class="py-6">
						We're working on adding new content. Please check back soon for our latest blog posts!
					</p>
					<button class="btn btn-primary" onclick={() => window.location.reload()}>
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
						Refresh Page
					</button>
				</div>
			</div>
		</div>
	{/if}

	<!-- Call to action -->
	<div class="card bg-primary text-primary-content mt-12 shadow-xl">
		<div class="card-body text-center">
			<h2 class="card-title mx-auto text-2xl">Subscribe to our Newsletter</h2>
			<p>Get the latest blog posts and updates delivered straight to your inbox.</p>
			<div class="mx-auto mt-4 flex w-full max-w-md flex-col items-center gap-2 sm:flex-row">
				<input type="email" placeholder="Your email address" class="input w-full" />
				<button class="btn bg-primary-content text-primary w-full sm:w-auto">Subscribe</button>
			</div>
		</div>
	</div>
</div>

<!-- Back to top button -->
<BackToTopButton />
