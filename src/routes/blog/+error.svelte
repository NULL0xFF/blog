<script lang="ts">
	import { page } from '$app/state';
</script>

<div class="hero bg-base-200 min-h-[60vh]">
	<div class="hero-content text-center">
		<div class="max-w-md">
			<h1 class="text-error text-5xl font-bold">Error {page.status}</h1>
			<div class="divider"></div>
			<p class="py-6">{page.error?.message || 'An unexpected error occurred'}</p>
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
			</div>
		</div>
	</div>
</div>
