<script lang="ts">
	import type { Post } from '$lib/types/blog';

	let { post } = $props<{ post: Post }>();
</script>

<div
	class="card bg-base-100 group h-full shadow-xl transition-all duration-300 hover:-translate-y-1 hover:shadow-2xl"
>
	{#if post.imageUrl}
		<figure class="overflow-hidden">
			<img
				src={post.imageUrl}
				alt={post.title}
				class="h-48 w-full object-cover transition-transform duration-500 group-hover:scale-110"
			/>
		</figure>
	{/if}

	<div class="card-body">
		<div class="flex items-start justify-between gap-2">
			<h2 class="card-title group-hover:text-primary transition-colors duration-300">
				{post.title}
			</h2>
			<div class="badge badge-primary">{new Date(post.date).toLocaleDateString()}</div>
		</div>

		{#if post.tags && post.tags.length > 0}
			<div class="my-2 flex flex-wrap gap-1">
				{#each post.tags as tag}
					<div class="badge badge-outline badge-sm hover:badge-primary transition-all duration-300">
						{tag}
					</div>
				{/each}
			</div>
		{/if}

		<p class="text-base-content/80">{post.description}</p>

		<div class="card-actions mt-auto items-center justify-between pt-4">
			{#if post.author}
				<div class="text-base-content/70 text-sm">
					By {post.author}
				</div>
			{/if}
			<a
				href={`/blog/${post.slug}`}
				class="btn btn-primary btn-sm btn-outline group-hover:btn-primary transition-all duration-300"
			>
				Read More
			</a>
		</div>
	</div>
</div>
