<script lang="ts">
	import type { PageData } from './$types';
	import BlogArticle from '$lib/components/BlogArticle.svelte';
	import CommentsSection from '$lib/components/CommentsSection.svelte';
	import BlogSidebar from '$lib/components/BlogSidebar.svelte';
	import RelatedPosts from '$lib/components/RelatedPosts.svelte';
	import BackToTopButton from '$lib/components/BackToTopButton.svelte';
	import type { Post } from '$lib/types/blog';

	let { data } = $props<{ data: PageData }>();
</script>

<div class="py-8">
	<!-- Blog post breadcrumbs for single post -->
	<div class="breadcrumbs mb-6 text-sm">
		<ul>
			<li><a href="/">Home</a></li>
			<li><a href="/blog">Blog</a></li>
			<li>{data.post?.title || 'Blog Post'}</li>
		</ul>
	</div>

	<!-- Display error if post is not available -->
	{#if !data.post}
		<div class="alert alert-error shadow-lg">
			<svg
				xmlns="http://www.w3.org/2000/svg"
				class="h-6 w-6 flex-shrink-0 stroke-current"
				fill="none"
				viewBox="0 0 24 24"
			>
				<path
					stroke-linecap="round"
					stroke-linejoin="round"
					stroke-width="2"
					d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
				/>
			</svg>
			<div>
				<h3 class="font-bold">Error</h3>
				<div class="text-xs">Failed to load blog post. Please try again later.</div>
			</div>
			<div class="flex-none">
				<a href="/blog" class="btn btn-sm">Return to Blog</a>
			</div>
		</div>
	{:else}
		<!-- Main content with sidebar layout -->
		<div class="flex flex-col gap-8 lg:flex-row">
			<!-- Main content area -->
			<div class="w-full lg:w-3/4">
				<!-- Blog post article component -->
				<BlogArticle post={data.post} />

				<!-- Related posts section -->
				{#if data.featuredPosts && data.featuredPosts.length > 0}
					{@const relatedPosts = data.featuredPosts
						.filter((p: Post) => p.slug !== data.post.slug)
						.slice(0, 3)}
					{#if relatedPosts.length > 0}
						<RelatedPosts posts={relatedPosts} />
					{/if}
				{/if}

				<!-- Divider before comments -->
				<div class="divider my-8">Comments</div>

				<!-- Comments section -->
				<CommentsSection />
			</div>

			<!-- Sidebar -->
			<div class="w-full lg:w-1/4">
				<BlogSidebar popularPosts={data.featuredPosts || []} />
			</div>
		</div>
	{/if}
</div>

<!-- Back to top button -->
<BackToTopButton />
