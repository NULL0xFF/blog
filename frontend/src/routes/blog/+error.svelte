<script lang="ts">
	import { page } from '$app/state';

	// Determine if the user is offline
	let isOffline = $state(false);

	// Function to check online status
	function checkConnection() {
		isOffline = typeof navigator !== 'undefined' ? !navigator.onLine : false;
	}

	// Run once when the component is mounted
	import { onMount } from 'svelte';

	onMount(() => {
		checkConnection();

		// Listen for online/offline events
		window.addEventListener('online', checkConnection);
		window.addEventListener('offline', checkConnection);

		return () => {
			window.removeEventListener('online', checkConnection);
			window.removeEventListener('offline', checkConnection);
		};
	});
</script>

<div class="hero bg-base-200 min-h-[60vh]">
	<div class="hero-content text-center">
		<div class="max-w-md">
			<h1 class="mb-4 text-4xl font-bold">
				{#if isOffline}
					You're Offline
				{:else if page.status === 404}
					Blog Post Not Found
				{:else}
					Blog Error
				{/if}
			</h1>

			<div class="divider"></div>

			<p class="py-6">
				{#if isOffline}
					Please check your internet connection and try again.
				{:else if page.status === 404}
					Sorry, we couldn't find the blog post you're looking for.
				{:else}
					{page.error?.message || 'An error occurred while loading blog content.'}
				{/if}
			</p>

			<div class="flex flex-wrap justify-center gap-4">
				<a href="/blog" class="btn btn-primary">
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

				<a href="/" class="btn btn-outline">
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
							d="M2.25 12l8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
						/>
					</svg>
					Go Home
				</a>

				{#if !isOffline && page.status !== 404}
					<button class="btn btn-outline" onclick={() => window.location.reload()}>
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
								d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.99"
							/>
						</svg>
						Try Again
					</button>
				{/if}
			</div>
		</div>
	</div>
</div>
