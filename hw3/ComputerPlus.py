'''
Created on Feb 2, 2010

@author: tim
for cs44 w10
'''
import sys
from Computer import Computer

class ComputerPlus(Computer):
    '''
    the computer player
    '''
        
    def minimaxi(self, s, order):
        """i like to make decisions -- I am the first player"""
        movevalues = {}
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move)
            if order == 1:
                movevalues[self.my_min(s, -sys.maxint -1, sys.maxint)] = move
            else:
                movevalues[self.my_max(s, -sys.maxint -1, sys.maxint)] = move
            s.undo_move(move)
        if order == 1:
            return movevalues[max(movevalues.keys())]
        else:
            return movevalues[min(movevalues.keys())]
    
#    def maximini(self, s):
#        """i like to make decisions -- I am the second player"""
#        movevalues = {}
#        for move in s._legal_moves():
#            self.statecounter = self.statecounter+1
#            s.do_move(move)
#            movevalues[self.my_max(s, -sys.maxint -1, sys.maxint)] = move
#            s.undo_move(move)
#        return movevalues[min(movevalues.keys())]
        
    
    #player
    def my_min(self, s,alpha, beta):
        """i like to minimize"""
        if self.cutoff_test(s):
            return self.utility(s, "min")
        self.counter = self.counter+1
        v = sys.maxint
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = min(v, self.my_max(s, alpha, beta)) #recurse
            s.undo_move(move) #undo the move when we return from the above
            if v <= alpha:
                return v
            else:
                beta = min(beta, v)
        return v

    #computer
    def my_max(self, s, alpha, beta):
        """i like to maximize!"""
        if self.cutoff_test(s):
            return self.utility(s, "max")
        self.counter = self.counter+1
        v = -sys.maxint - 1
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = max(v, self.my_min(s, alpha, beta)) #recurse
            s.undo_move(move) #undo the move when we return from the above
            if v >= beta:
                return v
            else:
                alpha = max(alpha, v)
        return v
        


        
        