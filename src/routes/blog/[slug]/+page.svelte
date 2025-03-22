<script lang="ts">
	import type { PageData } from './$types';
	import BlogArticle from '$lib/components/BlogArticle.svelte';
	import CommentsSection from '$lib/components/CommentsSection.svelte';
	import BlogSidebar from '$lib/components/BlogSidebar.svelte';
	import RelatedPosts from '$lib/components/RelatedPosts.svelte';
	import type { Post } from '$lib/types/blog';

	let { data } = $props<{ data: PageData }>();
</script>

<div class="py-8">
	<!-- Blog post breadcrumbs for single post -->
	<div class="breadcrumbs mb-6 text-sm">
		<ul>
			<li><a href="/">Home</a></li>
			<li><a href="/blog">Blog</a></li>
			<li>{data.post.title}</li>
		</ul>
	</div>

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
</div>
