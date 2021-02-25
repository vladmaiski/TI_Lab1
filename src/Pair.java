class Pair<E extends  Comparable<E>, K> implements Comparable<Pair<E, K>> {
    private E firstVal;
    private K secVal;

    public Pair(E firstVal, K secVal) {
        this.firstVal = firstVal;
        this.secVal = secVal;
    }

    public E getFirstVal() {
        return firstVal;
    }

    public K getSecVal() {
        return secVal;
    }

    public void setFirstVal(E firstVal) {
        this.firstVal = firstVal;
    }

    public void setSecVal(K secVal) {
        this.secVal = secVal;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "firstVal=" + firstVal +
                ", secVal=" + secVal +
                '}';
    }

    @Override
    public int compareTo(Pair<E, K> pair) {
        int first = Character.toUpperCase((Character)this.firstVal) - 'A';
        int second = Character.toUpperCase((Character)pair.firstVal) - 'A';
        return Integer.compare(first, second);
        /*
        return first.compateTo(second);
        return Character.toUpperCase((Character) this.firstVal).compareTo(pair.firstVal);*/
    }

}