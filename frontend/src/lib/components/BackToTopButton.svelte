<script>
	import { onMount } from 'svelte';

	// State to control the visibility of the button
	let isVisible = $state(false);

	// Function to check if the page has been scrolled
	function checkScroll() {
		const scrollY = window.scrollY;
		isVisible = scrollY > 300; // Show button when scrolled down 300px
	}

	// Function to scroll back to top
	function scrollToTop() {
		window.scrollTo({
			top: 0,
			behavior: 'smooth'
		});
	}

	// Add scroll event listener when component is mounted
	onMount(() => {
		window.addEventListener('scroll', checkScroll);

		// Clean up when component is destroyed
		return () => {
			window.removeEventListener('scroll', checkScroll);
		};
	});
</script>

<button
	class="btn btn-circle btn-primary fixed right-5 bottom-5 z-50 shadow-lg transition-opacity duration-300"
	style="opacity: {isVisible ? '1' : '0'}; pointer-events: {isVisible ? 'auto' : 'none'}"
	onclick={scrollToTop}
	aria-label="Back to top"
>
	<svg
		xmlns="http://www.w3.org/2000/svg"
		fill="none"
		viewBox="0 0 24 24"
		stroke-width="2.5"
		stroke="currentColor"
		class="h-6 w-6"
	>
		<path stroke-linecap="round" stroke-linejoin="round" d="M4.5 15.75l7.5-7.5 7.5 7.5" />
	</svg>
</button>
