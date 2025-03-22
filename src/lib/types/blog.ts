export interface Post {
  id: string | number;
  title: string;
  slug: string;
  description: string;
  content?: string;
  imageUrl: string;
  date: string;
  author?: string;
  tags?: string[];
}

export interface PaginatedResponse<T> {
  items: T[];
  currentPage: number;
  totalPages: number;
  totalItems: number;
  itemsPerPage: number;
}

export interface BlogPageData {
  posts: Post[];
  currentPage: number;
  totalPages: number;
  postsPerPage: number;
}

export interface PostPageData {
  post: Post;
}