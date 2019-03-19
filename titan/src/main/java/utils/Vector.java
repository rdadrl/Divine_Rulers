package utils;

public interface Vector<T> {
		public Vector<T> add(Vector<T> other);
		
		public Vector<T> substract(Vector<T> other);
		
		public Vector<T> scale(double c);

		public double dot(Vector<T> other);
		
		public double norm();
		
		public double length();
		
		public Vector<T> unit();
				
		public double dist(Vector<T> other);
		
		public boolean isOrthogonal(Vector<T> other);
		
}
