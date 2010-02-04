'''
Created on Feb 2, 2010

@author: tim
'''
import sys, random

class Computer(object):
    '''
    the computer player
    '''
    maxdepth = 100

    def __init__(self, s):
        """Constructor"""
        self.state = s  #the initial state
        self.counter = 0 #the depth
        
    def cutoff_test(self, s):
        """ returns true if we should stop"""
        (b, p) = s.is_win()
        return b or s.legal_moves() == "" or self.counter >= self.maxdepth

#        1. We have reached a terminal state (a win or a draw) 
#        2. We have reached the specified maximum depth. 
#        3. We have visited the specified maximum number of states.  TODO: why?
    
    
            
    
    def minimaxi(self, s):
        """i like to make decisions"""
        movevalues = {}
        for move in s._legal_moves():
            s.do_move(move)
            movevalues[self.my_min(s)] = move
            s.undo_move(move)
        return movevalues[max(movevalues.keys())]
        
    
    #player
    def my_min(self, s,):
        """i like to minimize"""
        if self.cutoff_test(s):
            return self.utility(s, "min")
        self.counter = self.counter+1
        v = sys.maxint
        for move in s._legal_moves():
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
            s.do_move(move) #do the move
            v = max(v, self.my_min(s)) #recurse
            s.undo_move(move) #undo the move when we return from the above
        return v
        
    def utility(self,s, p):
        """the utility function"""
        if s.is_win() and p == "max":
            return sys.maxint
        elif s.is_win() and p == "min":
            return -sys.maxint -1
        elif s.legal_moves() == "":
            return 0
        else:
            print " do we ever?!??!?!?!?!?!"
            return random.randint(-(sys.maxint/2), (sys.maxint/2))
            
        
        