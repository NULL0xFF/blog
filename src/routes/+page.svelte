<script lang="ts">
	
	// The error was related to passing a type argument to $props()
	// In Svelte 5, we need to specify the type differently
	let { data } = $props();
</script>

<div class="container mx-auto py-8">
	<h1 class="mb-8 text-center text-3xl font-bold">Latest Blog Posts</h1>

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
</div>
