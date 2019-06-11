package utils.vector;

public interface Vector<T>{
        Vector add(Vector<T> other);

        Vector substract(Vector<T> other);

        Vector<T> scale(double c);

        double dot(Vector<T> other);
		
        double norm();
		
        double length();
		
        Vector<T> unit();
				
        double dist(Vector<T> other);
		
        boolean isOrthogonal(Vector<T> other);
		
}
