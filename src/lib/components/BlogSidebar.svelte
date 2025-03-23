<script lang="ts">
	import BlogCategories from './BlogCategories.svelte';
	import type { Post } from '$lib/types/blog';

	let { popularPosts = [] } = $props<{
		popularPosts?: Post[];
	}>();
</script>

<div class="flex flex-col gap-6">
	<!-- Categories section -->
	<BlogCategories />

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

			<div class="flex flex-wrap gap-2">
				<a href="/blog?tag=svelte" class="badge badge-outline">svelte</a>
				<a href="/blog?tag=javascript" class="badge badge-outline">javascript</a>
				<a href="/blog?tag=typescript" class="badge badge-outline">typescript</a>
				<a href="/blog?tag=web" class="badge badge-outline">web</a>
				<a href="/blog?tag=development" class="badge badge-outline">development</a>
				<a href="/blog?tag=daisyui" class="badge badge-outline">daisyui</a>
				<a href="/blog?tag=tailwind" class="badge badge-outline">tailwind</a>
				<a href="/blog?tag=css" class="badge badge-outline">css</a>
				<a href="/blog?tag=html" class="badge badge-outline">html</a>
				<a href="/blog?tag=api" class="badge badge-outline">api</a>
				<a href="/blog?tag=design" class="badge badge-outline">design</a>
			</div>
		</div>
	</div>

	<!-- Archive section -->
	<div class="card bg-base-100 shadow-lg">
		<div class="card-body">
			<h3 class="card-title text-lg">Archives</h3>
			<div class="divider mt-0"></div>

			<ul class="menu menu-sm p-0">
				<li><a href="/blog?month=2023-12">December 2023</a></li>
				<li><a href="/blog?month=2023-11">November 2023</a></li>
				<li><a href="/blog?month=2023-10">October 2023</a></li>
				<li><a href="/blog?month=2023-09">September 2023</a></li>
				<li><a href="/blog?month=2023-08">August 2023</a></li>
			</ul>
		</div>
	</div>
</div>
