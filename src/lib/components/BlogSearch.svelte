<script lang="ts">
	// In a real app, you would implement actual search functionality
	// This is a placeholder UI component

	let searchTerm = $state('');
	let isSearching = $state(false);

	function handleSearch(e: Event) {
		e.preventDefault();
		if (!searchTerm.trim()) return;

		isSearching = true;

		// Simulate search delay
		setTimeout(() => {
			isSearching = false;
			// In a real app, you would handle the search results here
		}, 1000);
	}
</script>

<div class="card bg-base-100 mb-8 shadow-lg">
	<div class="card-body p-4">
		<form onsubmit={handleSearch} class="w-full">
			<div class="input-group w-full">
				<input
					type="text"
					bind:value={searchTerm}
					placeholder="Search blog posts..."
					class="input input-bordered w-full"
					disabled={isSearching}
				/>
				<button type="submit" class="btn btn-primary" disabled={isSearching || !searchTerm.trim()}>
					{#if isSearching}
						<span class="loading loading-spinner loading-sm"></span>
					{:else}
						<svg
							xmlns="http://www.w3.org/2000/svg"
							class="h-5 w-5"
							fill="none"
							viewBox="0 0 24 24"
							stroke="currentColor"
						>
							<path
								stroke-linecap="round"
								stroke-linejoin="round"
								stroke-width="2"
								d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
							/>
						</svg>
					{/if}
				</button>
			</div>

			<!-- Search filters - you can expand these in a real app -->
			<div class="mt-2 flex flex-wrap gap-2">
				<div class="dropdown dropdown-hover">
					<button type="button" class="btn btn-sm btn-ghost">
						Categories
						<svg
							xmlns="http://www.w3.org/2000/svg"
							fill="none"
							viewBox="0 0 24 24"
							stroke-width="1.5"
							stroke="currentColor"
							class="h-4 w-4"
						>
							<path
								stroke-linecap="round"
								stroke-linejoin="round"
								d="M19.5 8.25l-7.5 7.5-7.5-7.5"
							/>
						</svg>
					</button>
					<ul class="dropdown-content bg-base-100 rounded-box z-[1] w-52 p-2 shadow-md">
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Technology</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Design</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Development</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Business</button
							>
						</li>
					</ul>
				</div>

				<div class="dropdown dropdown-hover">
					<button type="button" class="btn btn-sm btn-ghost">
						Date
						<svg
							xmlns="http://www.w3.org/2000/svg"
							fill="none"
							viewBox="0 0 24 24"
							stroke-width="1.5"
							stroke="currentColor"
							class="h-4 w-4"
						>
							<path
								stroke-linecap="round"
								stroke-linejoin="round"
								d="M19.5 8.25l-7.5 7.5-7.5-7.5"
							/>
						</svg>
					</button>
					<ul class="dropdown-content bg-base-100 rounded-box z-[1] w-52 p-2 shadow-md">
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Last week</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Last month</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Last year</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Custom range</button
							>
						</li>
					</ul>
				</div>

				<div class="dropdown dropdown-hover">
					<button type="button" class="btn btn-sm btn-ghost">
						Sort By
						<svg
							xmlns="http://www.w3.org/2000/svg"
							fill="none"
							viewBox="0 0 24 24"
							stroke-width="1.5"
							stroke="currentColor"
							class="h-4 w-4"
						>
							<path
								stroke-linecap="round"
								stroke-linejoin="round"
								d="M19.5 8.25l-7.5 7.5-7.5-7.5"
							/>
						</svg>
					</button>
					<ul class="dropdown-content bg-base-100 rounded-box z-[1] w-52 p-2 shadow-md">
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Most Recent</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Most Popular</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>A-Z</button
							>
						</li>
						<li>
							<button type="button" class="hover:bg-base-200 block w-full rounded-lg p-2 text-left"
								>Z-A</button
							>
						</li>
					</ul>
				</div>
			</div>
		</form>
	</div>
</div>
