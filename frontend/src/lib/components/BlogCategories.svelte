<script lang="ts">
	// In a real application, you would fetch these categories from the API
	const categories = [
		{ name: 'Technology', count: 12, color: 'badge-primary' },
		{ name: 'Development', count: 8, color: 'badge-secondary' },
		{ name: 'Design', count: 5, color: 'badge-accent' },
		{ name: 'Business', count: 3, color: 'badge-info' },
		{ name: 'Marketing', count: 4, color: 'badge-success' },
		{ name: 'Tutorials', count: 7, color: 'badge-warning' }
	];
</script>

<div class="card bg-base-100 shadow-lg">
	<div class="card-body">
		<h3 class="card-title text-lg">Categories</h3>
		<div class="divider mt-0"></div>

		<div class="flex flex-wrap gap-2">
			{#each categories as category}
				<a
					href={`/blog?category=${encodeURIComponent(category.name.toLowerCase())}`}
					class="badge {category.color} badge-lg hover:badge-ghost gap-1 transition-colors duration-300"
				>
					{category.name}
					<span class="badge badge-sm">{category.count}</span>
				</a>
			{/each}
		</div>
	</div>
</div>
