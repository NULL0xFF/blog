<script lang="ts">
	// Simulated comments data - in a real app, this would come from your API
	let comments = $state([
		{
			id: 1,
			name: 'Alex Johnson',
			date: '2023-12-10T14:23:01Z',
			content: 'Great article! I especially liked the part about component design patterns.',
			avatar: 'https://i.pravatar.cc/150?img=1',
			replies: []
		},
		{
			id: 2,
			name: 'Sarah Miller',
			date: '2023-12-09T09:45:22Z',
			content: "Thanks for sharing this information. It's really helpful for my current project.",
			avatar: 'https://i.pravatar.cc/150?img=2',
			replies: [
				{
					id: 21,
					name: 'Author',
					date: '2023-12-09T10:12:45Z',
					content: "You're welcome, Sarah! I'm glad you found it useful.",
					avatar: 'https://i.pravatar.cc/150?img=3',
					isAuthor: true
				}
			]
		}
	]);

	// Form state
	let newComment = $state('');
	let name = $state('');
	let email = $state('');
	let isSubmitting = $state(false);

	function submitComment(e: Event) {
		e.preventDefault();
		if (!newComment.trim() || !name.trim() || !email.trim()) return;

		isSubmitting = true;

		// Simulate API call delay
		setTimeout(() => {
			const newCommentObj = {
				id: comments.length + 1,
				name: name,
				date: new Date().toISOString(),
				content: newComment,
				avatar: `https://i.pravatar.cc/150?img=${Math.floor(Math.random() * 70)}`,
				replies: []
			};

			comments = [newCommentObj, ...comments];

			// Reset form
			newComment = '';
			isSubmitting = false;
		}, 1000);
	}
</script>

<section class="py-8">
	<h2 class="mb-6 text-2xl font-bold">Comments ({comments.length})</h2>

	<!-- Comment form -->
	<div class="card bg-base-100 mb-8 shadow-lg">
		<div class="card-body">
			<h3 class="card-title text-lg">Leave a comment</h3>
			<div class="divider mt-0"></div>

			<form onsubmit={submitComment} class="space-y-4">
				<div class="grid grid-cols-1 gap-4 md:grid-cols-2">
					<div class="form-control w-full">
						<label for="comment-name" class="label">
							<span class="label-text">Name</span>
						</label>
						<input
							id="comment-name"
							type="text"
							bind:value={name}
							placeholder="Your name"
							class="input input-bordered w-full"
							required
						/>
					</div>

					<div class="form-control w-full">
						<label for="comment-email" class="label">
							<span class="label-text">Email</span>
						</label>
						<input
							id="comment-email"
							type="email"
							bind:value={email}
							placeholder="Your email (not published)"
							class="input input-bordered w-full"
							required
						/>
					</div>
				</div>

				<div class="form-control w-full">
					<label for="comment-text" class="label">
						<span class="label-text">Comment</span>
					</label>
					<textarea
						id="comment-text"
						bind:value={newComment}
						class="textarea textarea-bordered h-24"
						placeholder="Share your thoughts..."
						required
					></textarea>
				</div>

				<div class="flex justify-end">
					<button type="submit" class="btn btn-primary" disabled={isSubmitting}>
						{#if isSubmitting}
							<span class="loading loading-spinner loading-sm"></span>
							Submitting...
						{:else}
							Post Comment
						{/if}
					</button>
				</div>
			</form>
		</div>
	</div>

	<!-- Comments list -->
	<div class="space-y-6">
		{#each comments as comment}
			<div class="card bg-base-100 shadow-lg">
				<div class="card-body">
					<div class="flex items-start gap-4">
						<div class="avatar">
							<div class="w-12 rounded-full">
								<img src={comment.avatar} alt={comment.name} />
							</div>
						</div>

						<div class="flex-1">
							<div class="mb-2 flex items-center justify-between">
								<div>
									<h4 class="font-bold">{comment.name}</h4>
									<p class="text-base-content/70 text-xs">
										{new Date(comment.date).toLocaleDateString()} at {new Date(
											comment.date
										).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
									</p>
								</div>

								<button class="btn btn-ghost btn-sm"> Reply </button>
							</div>

							<p class="mt-2">{comment.content}</p>

							<!-- Replies -->
							{#if comment.replies.length > 0}
								<div class="border-base-300 mt-4 border-l-2 pl-6">
									{#each comment.replies as reply}
										<div class="mt-4 flex items-start gap-4">
											<div class="avatar">
												<div class="w-10 rounded-full">
													<img src={reply.avatar} alt={reply.name} />
												</div>
											</div>

											<div class="flex-1">
												<div class="flex items-center justify-between">
													<div>
														<h5 class="font-bold">
															{reply.name}
															{#if reply.isAuthor}
																<span class="badge badge-primary badge-sm ml-1">Author</span>
															{/if}
														</h5>
														<p class="text-base-content/70 text-xs">
															{new Date(reply.date).toLocaleDateString()} at {new Date(
																reply.date
															).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
														</p>
													</div>
												</div>

												<p class="mt-2">{reply.content}</p>
											</div>
										</div>
									{/each}
								</div>
							{/if}
						</div>
					</div>
				</div>
			</div>
		{/each}
	</div>
</section>
