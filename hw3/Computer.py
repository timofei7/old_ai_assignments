'''
Created on Feb 2, 2010

@author: tim
for cs44 w10
'''
import sys, random

class Computer(object):
    '''
    the computer player
    '''
    maxdepth = 1000
    maxstates = 100000

    def __init__(self, s):
        """Constructor"""
        self.state = s  #the initial state
        self.counter = 0 #the depth searched
        self.statecounter = 0 #states examined
        
    def cutoff_test(self, s):
        """ returns true if we should stop"""        
#        1. We have reached a terminal state (a win or a draw) 
#        2. We have reached the specified maximum depth. 
#        3. We have visited the specified maximum number of states.
        (b, p) = s.is_win()
        return b or s.legal_moves() == "" or self.counter >= self.maxdepth or self.statecounter >= self.maxstates
            
    
    def minimaxi(self, s, order):
        """i like to make computer like decisions"""
        self.counter = 0;      #reset the counters
        self.statecounter =0;  #reset the counters
        movevalues = {}
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move)
            if order == 1: #if i go first i'm maxi
                movevalues[self.my_min(s)] = move
            else: #if i go second i'm mini
                movevalues[self.my_max(s)] = move
            s.undo_move(move)
        if order == 1:
            return movevalues[max(movevalues.keys())]
        else:
            return movevalues[min(movevalues.keys())]
        
        
    
    #player
    def my_min(self, s,):
        """i like to minimize"""
        if self.cutoff_test(s):
            return self.utility(s, "min")
        self.counter = self.counter+1
        v = sys.maxint
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = min(v, self.my_max(s)) #recurse
            s.undo_move(move) #undo the move when we return from the above
        return v

    #computer
    def my_max(self, s):
        """i like to maximize!"""
        if self.cutoff_test(s):
            return self.utility(s, "max")
        self.counter = self.counter+1
        v = -sys.maxint - 1
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = max(v, self.my_min(s)) #recurse
            s.undo_move(move) #undo the move when we return from the above
        return v
        
    def utility(self, s, p):
        """the utility function"""
        
        segs = s.build_segments()
        
        val = 0
        
        (b, np) = s._is_win(segs)
        if b and p == "max":
            val =  sys.maxint#-sys.maxint-1
        elif b and p == "min":
            val = -sys.maxint -1#sys.maxint
        elif s.legal_moves() == "":
            val = 0
        else:
            val = self.evaluate(segs)
            #return random.randint(-(sys.maxint/2), (sys.maxint/2))
        return val
            
            
    def evaluate(self, segs):
        """the evaluation function for minimax"""
        
        def e(seg, pos):
            """0 if s contains no disks or disks from both players,
               1 if s contains one X,
               10 if s contains two X's
               100 if s contains three X's"""
               
            
            if seg.count(pos) == 1 and seg.count(".") == 3:
                return 1
            elif seg.count(pos) == 2 and seg.count(".") == 2:
                return 10
            elif seg.count(pos) == 3 and seg.count(".") == 1:
                return 100
            elif seg.count(".") == 4:
                return 0
            else:
                return 0
    
        val = 0
        
        for seg in segs:
            val = val + e(seg, "X")
            val = val - e(seg, "O")
            
        return val    
        
        