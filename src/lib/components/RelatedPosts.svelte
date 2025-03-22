<script lang="ts">
	import type { Post } from '$lib/types/blog';

	let { posts, title = 'Related Posts' } = $props<{
		posts: Post[];
		title?: string;
	}>();
</script>

{#if posts && posts.length > 0}
	<section class="py-8">
		<h2 class="mb-6 text-2xl font-bold">{title}</h2>

		<div class="grid grid-cols-1 gap-6 md:grid-cols-3">
			{#each posts as post}
				<div class="card bg-base-100 shadow-lg transition-all duration-300 hover:shadow-xl">
					{#if post.imageUrl}
						<figure>
							<img src={post.imageUrl} alt={post.title} class="h-40 w-full object-cover" />
						</figure>
					{/if}

					<div class="card-body p-4">
						<h3 class="card-title text-base">{post.title}</h3>
						<p class="text-base-content/70 mt-1 text-sm">
							{new Date(post.date).toLocaleDateString()}
						</p>

						<div class="card-actions mt-4 justify-end">
							<a href={`/blog/${post.slug}`} class="btn btn-sm btn-outline">Read More</a>
						</div>
					</div>
				</div>
			{/each}
		</div>
	</section>
{/if}
