import java.util.*;

class LFUCache<T,U> {
    // step 1: initialize usuable variable
    int capacity; // to know the capacity of cache
    Map<T,Node> cache; // cache where given key value pairs are exists or not
    Map<Integer,DoubleLinkedList> freqMap; // to know the hit condition of key
    int minFrequency;
    int currSize;
    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFrequency = 0;
        this.currSize =0;
    }
    
    public U get(T key) {
        try{
            Node node = cache.get(key);
            updateNode(node);
            return node.value;
        }catch(Exception e){
            System.out.println("key Not Found : " +e); 
            return null;
        }
    }
    
    public void put(T key, U value) {
        if(this.capacity ==0){
            return;
        }
        if(this.cache.containsKey(key)){
            Node node = this.cache.get(key);
            node.value = value;
            this.updateNode(node);
        }else{
            this.currSize++;
            if(this.currSize > this.capacity){
                // ifcurrent size is > capacity then, find min freq node and remove it from list   
                DoubleLinkedList  minfreqList = this.freqMap.get(this.minFrequency);
                this.cache.remove(minfreqList.tail.prev.key);
                minfreqList.removeNode(minfreqList.tail.prev);
            }
            //create new Node 
            Node newNode = new Node(key,value);
            this.minFrequency = 1;
            DoubleLinkedList newList = this.freqMap.getOrDefault(1,new DoubleLinkedList());
            newList.addNode(newNode);
            this.freqMap.put(1,newList);
            this.cache.put(newNode.key,newNode);
        }
    }


    // step 2: create Node that store key , value, frequency of item is recently called
    class Node{
        T key;
        U value;
        int freq;
        Node prev,next;
        public Node(){}
        public Node(T key,U value){
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }
    // end of class Node

    // step 3: creating Double LinkedList Data structure
    class DoubleLinkedList{
        int size; // to know how many nodes in DLL 
        Node head,tail; // first last nodes 
        public DoubleLinkedList(){
            this.size =0;
            this.head = new Node(); // pointing first of dll
            this.tail = new Node(); // pointing last of dll 
            head.next = tail;
            tail.prev = head;
        }
        // next addNode to DLL 
        public void addNode(Node node){
            // we need add the node first because frequently called node
            try{
                Node temp = head.next;
                // add node next to head
                head.next = node;
                node.prev = head; 
                // add temp next to node 
                node.next = temp;
                temp.prev = node;
                // task is completed
                this.size++;
                  

            }catch(Exception e){
               System.out.println("Error in Removing Node from DLL: "+ e );
                
            }
           
        }
        //  end of add Node

        public void removeNode(Node node){
            // here we remove the node last of before the tail 
            if(node == null){
                return;
            }
            try{
                node.prev.next = node.next;
                node.next.prev= node.prev;
                this.size--;
            }catch(Exception e){
                System.out.println("Error in Removing Node from DLL: "+ e );
                
            }
        }
    }
    // end of class  DoubleLinked List 
    
    // step 4 : updating Node 
    public boolean updateNode(Node node){
        try{
        // get the dll to node 
        int minFreq = node.freq;
        DoubleLinkedList nodelist = this.freqMap.get(node.freq);
        nodelist.removeNode(node);
        if(minFreq == this.minFrequency && nodelist.size == 0){
            this.minFrequency++;
        }
        node.freq++;
        DoubleLinkedList newList = this.freqMap.getOrDefault(node.freq, new DoubleLinkedList());
        newList.addNode(node);
        this.freqMap.put(node.freq, newList);
        this.cache.put(node.key, node);
            return true;
        }catch(Exception e){
            System.out.println("Error in  Updating Cache"+e);
            return false;
        }
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
//  */

class lfu_design_java{
    public static void main(String args[]){
        
       LFUCache<Integer,Integer> lfu = new LFUCache<>(3);
        lfu.put(3,3);
        lfu.put(4,2);
        lfu.get(4);
    try{
        System.out.println(lfu.get(3));
        System.out.println(lfu.get(2));
      }catch(Exception e){
    System.out.println(e);
}
LFUCache<String,String> lfu1 = new LFUCache<>(3);
lfu1.put("hai","hello");
lfu1.put("bye","toy");
try{
System.out.println(lfu1.get("hai"));
System.out.println(lfu1.get("noi"));
}catch(Exception e){
System.out.println(e);
}
}
// memory 139MB
// cpu time 2194ms 
// Averge time complexicity get and put is O(1)
}

