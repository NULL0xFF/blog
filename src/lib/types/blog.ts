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

export interface Reply {
  id: number;
  name: string;
  date: string;
  content: string;
  avatar: string;
  isAuthor?: boolean;
}

export interface Comment {
  id: number;
  name: string;
  date: string;
  content: string;
  avatar: string;
  replies: Reply[];
}