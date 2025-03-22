<script lang="ts">
	import type { LayoutData } from './$types';
	import FeaturedPosts from '$lib/components/FeaturedPosts.svelte';

	let { data, children } = $props<{ data: LayoutData; children: any }>();
</script>

<div class="container mx-auto">
	<!-- Blog header with breadcrumbs -->
	<div class="breadcrumbs py-4 text-sm">
		<ul>
			<li><a href="/">Home</a></li>
			<li>Blog</li>
		</ul>
	</div>

	<!-- Main content area -->
	{@render children()}

	<!-- Display featured posts (only when we're not already on a featured post page) -->
	{#if data.featuredPosts && !data.post}
		<div class="divider my-8">Continue Reading</div>
		<FeaturedPosts posts={data.featuredPosts} />
	{/if}

	<!-- Blog footer with newsletter signup -->
	<div class="card bg-base-200 my-12 shadow-lg">
		<div class="card-body">
			<h2 class="card-title text-xl">Subscribe to our newsletter</h2>
			<p>Stay updated with our latest blog posts and news.</p>
			<div class="form-control mt-4">
				<div class="input-group">
					<input
						id="newsletter-email"
						type="email"
						placeholder="your-email@example.com"
						class="input input-bordered w-full"
					/>
					<button class="btn btn-primary">Subscribe</button>
				</div>
				<label for="newsletter-email" class="label">
					<span class="label-text-alt"
						>We respect your privacy and will never share your email.</span
					>
				</label>
			</div>
		</div>
	</div>
</div>
