'''
Created on Feb 2, 2010

@author: tim
'''

import sys

class Computer(object):
    '''
    the computer player
    '''
    maxdepth = 1000

    def __init__(self, s):
        """Constructor"""
        self.state = s  #the initial state
        self.counter = 0 #the depth
        
    def cutoff_test(self, s, c):
        """ returns true if we should stop"""
        (b,) = s.is_win()
        return b or s.legal_moves() == "" or c >= self.maxdepth

#        1. We have reached a terminal state (a win or a draw) 
#        2. We have reached the specified maximum depth. 
#        3. We have visited the specified maximum number of states.  TODO: what?
    
    def minimaxi(self):
        pass
    
    def my_min(self, s, c):
        if self.cutoff_test(s, c):
            return self.utility(s)
        v = sys.maxint
        for action in s._legal_moves():
            v = min(v, self.my_max(s.do_move(action)))
        return v

    def my_max(self, s, c):
        if self.cutoff_test(s, c):
            return self.utility(s)
        v = -sys.maxint - 1
        for action in s._legal_moves():
            v = max(v, self.my_min(s.do_move(action)))
        return v
        
    def utility(self,s):
        return 1
        
        