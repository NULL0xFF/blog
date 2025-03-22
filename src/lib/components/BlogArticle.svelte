<script lang="ts">
	import type { Post } from '$lib/types/blog';

	let { post } = $props<{ post: Post }>();

	// Format the date using a more detailed format
	const formatDate = (dateString: string): string => {
		const date = new Date(dateString);
		return date.toLocaleDateString(undefined, {
			year: 'numeric',
			month: 'long',
			day: 'numeric'
		});
	};
</script>

<article class="card bg-base-100 overflow-hidden shadow-xl">
	{#if post.imageUrl}
		<figure>
			<img
				src={post.imageUrl}
				alt={post.title}
				class="h-64 w-full object-cover transition-transform duration-500 hover:scale-105 md:h-80"
			/>
		</figure>
	{/if}

	<div class="card-body">
		<div class="flex flex-col gap-4">
			<!-- Title -->
			<h1 class="text-4xl font-bold">{post.title}</h1>

			<!-- Metadata row -->
			<div class="text-base-content/70 flex flex-wrap items-center gap-4">
				<div class="badge badge-primary">{formatDate(post.date)}</div>

				{#if post.author}
					<div class="flex items-center gap-2">
						<div class="avatar placeholder">
							<div class="bg-neutral text-neutral-content w-8 rounded-full">
								<span class="text-xs">{post.author.charAt(0).toUpperCase()}</span>
							</div>
						</div>
						<span>{post.author}</span>
					</div>
				{/if}

				{#if post.tags && post.tags.length > 0}
					<div class="flex flex-wrap gap-1">
						{#each post.tags as tag}
							<span class="badge badge-outline">{tag}</span>
						{/each}
					</div>
				{/if}
			</div>
		</div>

		<!-- Article content -->
		<div class="divider"></div>

		<div
			class="prose lg:prose-lg prose-img:rounded-xl prose-headings:text-primary prose-a:text-secondary max-w-none"
		>
			{@html post.content}
		</div>

		<div class="divider"></div>

		<!-- Post navigation and sharing -->
		<div class="mt-8 flex flex-wrap justify-between gap-2">
			<a href="/blog" class="btn btn-outline">
				<svg
					xmlns="http://www.w3.org/2000/svg"
					fill="none"
					viewBox="0 0 24 24"
					stroke-width="1.5"
					stroke="currentColor"
					class="mr-1 h-5 w-5"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18"
					/>
				</svg>
				Back to Blog
			</a>

			<div class="flex gap-2">
				<button class="btn btn-circle btn-outline" aria-label="Share post">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						fill="none"
						viewBox="0 0 24 24"
						stroke-width="1.5"
						stroke="currentColor"
						class="h-5 w-5"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							d="M7.217 10.907a2.25 2.25 0 100 2.186m0-2.186c.18.324.283.696.283 1.093s-.103.77-.283 1.093m0-2.186l9.566-5.314m-9.566 7.5l9.566 5.314m0 0a2.25 2.25 0 103.935 2.186 2.25 2.25 0 00-3.935-2.186zm0-12.814a2.25 2.25 0 103.933-2.185 2.25 2.25 0 00-3.933 2.185z"
						/>
					</svg>
				</button>
				<button class="btn btn-circle btn-outline" aria-label="Like post">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						fill="none"
						viewBox="0 0 24 24"
						stroke-width="1.5"
						stroke="currentColor"
						class="h-5 w-5"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z"
						/>
					</svg>
				</button>
			</div>
		</div>
	</div>
</article>
