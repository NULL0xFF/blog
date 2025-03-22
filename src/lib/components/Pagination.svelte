<script lang="ts">
	let {
		currentPage,
		totalPages,
		basePath = '/blog'
	} = $props<{
		currentPage: number;
		totalPages: number;
		basePath?: string;
	}>();

	// Calculate which pages to show (show up to 5 page buttons)
	function getPageNumbers() {
		let pages: number[] = [];

		// If we have 7 or fewer pages, show all of them
		if (totalPages <= 7) {
			pages = Array.from({ length: totalPages }, (_, i) => i + 1);
		} else {
			// Always include first page
			pages.push(1);

			// Calculate start and end of the page range
			let startPage = Math.max(2, currentPage - 2);
			let endPage = Math.min(totalPages - 1, currentPage + 2);

			// Add ellipsis indicator if needed
			if (startPage > 2) {
				pages.push(-1); // -1 represents ellipsis
			}

			// Add the page range
			for (let i = startPage; i <= endPage; i++) {
				pages.push(i);
			}

			// Add ellipsis indicator if needed
			if (endPage < totalPages - 1) {
				pages.push(-2); // -2 represents ellipsis
			}

			// Always include last page
			pages.push(totalPages);
		}

		return pages;
	}
</script>

{#if totalPages > 1}
	<div class="join mt-10 flex items-center justify-center">
		<!-- Previous page button -->
		<a
			href={`${basePath}?page=${currentPage - 1}`}
			class="join-item btn"
			class:btn-disabled={currentPage === 1}
			aria-label="Previous page"
		>
			«
		</a>

		<!-- Page number buttons -->
		{#each getPageNumbers() as pageNum}
			{#if pageNum < 0}
				<!-- Ellipsis -->
				<span class="join-item btn btn-disabled">...</span>
			{:else}
				<a
					href={`${basePath}?page=${pageNum}`}
					class="join-item btn"
					class:btn-active={currentPage === pageNum}
					aria-current={currentPage === pageNum ? 'page' : undefined}
				>
					{pageNum}
				</a>
			{/if}
		{/each}

		<!-- Next page button -->
		<a
			href={`${basePath}?page=${currentPage + 1}`}
			class="join-item btn"
			class:btn-disabled={currentPage === totalPages}
			aria-label="Next page"
		>
			»
		</a>
	</div>
{/if}
