import adapter from '@sveltejs/adapter-static';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/kit').Config} */
const config = {
  preprocess: vitePreprocess(),

  kit: {
    adapter: adapter({
      // Output directory for the static build
      fallback: 'index.html', // Optional: For SPA-style routing
      precompress: false     // Optional: Set to true to precompress files
    })
  }
};

export default config;
