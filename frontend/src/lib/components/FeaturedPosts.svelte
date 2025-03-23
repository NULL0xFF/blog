<script lang="ts">
  import type { Post } from '$lib/types/blog';
  
  let { posts, title = 'Featured Posts' } = $props<{ 
    posts: Post[],
    title?: string 
  }>();
</script>

{#if posts && posts.length > 0}
  <div class="py-8">
    <h2 class="text-2xl font-bold mb-6">{title}</h2>
    
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {#each posts as post}
        <div class="card bg-base-100 shadow-xl hover:shadow-2xl transition-all duration-300 h-full">
          {#if post.imageUrl}
            <figure>
              <img src={post.imageUrl} alt={post.title} class="h-40 w-full object-cover" />
            </figure>
          {/if}
          
          <div class="card-body">
            <h3 class="card-title text-lg">{post.title}</h3>
            
            {#if post.tags && post.tags.length > 0}
              <div class="flex flex-wrap gap-1 mt-1 mb-2">
                {#each post.tags.slice(0, 2) as tag}
                  <div class="badge badge-sm">{tag}</div>
                {/each}
              </div>
            {/if}
            
            <p class="line-clamp-2 text-sm text-base-content/80">{post.description}</p>
            
            <div class="card-actions justify-end mt-auto pt-2">
              <a href={`/blog/${post.slug}`} class="btn btn-sm btn-ghost">Read more →</a>
            </div>
          </div>
        </div>
      {/each}
    </div>
  </div>
{/if}