<script lang="ts">
	import { page } from '$app/state';
</script>

<div class="py-8">
	<div class="card bg-base-100 shadow-xl">
		<div class="card-body text-center">
			<h1 class="card-title mx-auto mb-6 text-3xl">
				{#if page.status === 404}
					Post Not Found
				{:else}
					Error Loading Post
				{/if}
			</h1>

			<div class="my-8 text-6xl">
				{#if page.status === 404}
					📄
				{:else}
					⚠️
				{/if}
			</div>

			<p class="mb-8 text-lg">
				{#if page.status === 404}
					Sorry, we couldn't find the blog post you're looking for. It may have been moved or
					deleted.
				{:else}
					{page.error?.message || 'An error occurred while loading this blog post.'}
				{/if}
			</p>

			<div class="flex flex-wrap justify-center gap-4">
				<a href="/blog" class="btn btn-primary"> Browse All Posts </a>

				<a href="/" class="btn btn-outline"> Return to Home </a>

				{#if page.status !== 404}
					<button class="btn btn-outline" onclick={() => window.location.reload()}>
						Try Again
					</button>
				{/if}
			</div>
		</div>
	</div>
</div>
