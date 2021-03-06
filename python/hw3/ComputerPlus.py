'''
Created on Feb 2, 2010

@author: tim
for cs44 w10
'''
import sys
from Computer import Computer

class ComputerPlus(Computer):
    '''
    the computer player but with alpha-beta pruning
    '''
    
    def minimaxi(self, s, who):
        """i like to make computer like decisions"""
        self.maxdepth = 150
        self.maxstates = 15000
        self.statecounter =0;  #reset the counter
        movevalues = {}
        depth = 0
        finding = True
        while (finding):  # iterative deepening
            self.depthcount = 0 #reset the depthcounter
            for move in s._legal_moves():
                self.statecounter = self.statecounter+1
                s.do_move(move)
                if who == "X": #if i go first i'm maxi
                    movevalues[self.my_min(s,depth, move, -sys.maxint -1, sys.maxint)] = move
                else: #if i go second i'm mini
                    movevalues[self.my_max(s,depth, move, -sys.maxint -1, sys.maxint)] = move
                s.undo_move(move)
            if depth >= self.maxdepth:
                finding = False  #stop and return result if we've found a win... TODO: correct?
            else:
                depth = depth +1
        print "ab    state count: " + str(self.statecounter) + " depth: " + str(depth)
        if who == "X":
            print "    eval move: " + str(max(movevalues.keys()))
            return movevalues[max(movevalues.keys())]
        else:
            print "    eval move: " + str(min(movevalues.keys()))
            return movevalues[min(movevalues.keys())]
                
    
    def my_min(self, s,depth, orig, alpha, beta):
        """i like to minimize"""
        if self.cutoff_test(s, depth):
            return self.utility(s)
        v = sys.maxint - 1
        self.depthcount = self.depthcount + 1
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = min(v, self.my_max(s,depth, orig, alpha, beta)) #recurse
            s.undo_move(move) #undo the move when we return from the above
            if v <= alpha:#   
                self.depthcount = self.depthcount - 1
                return v
            else:
                beta = min(beta, v)
        self.depthcount = self.depthcount - 1
        return v

    def my_max(self, s,depth, orig, alpha, beta):
        """i like to maximize!"""
        if self.cutoff_test(s, depth):
            return self.utility(s)
        v = -sys.maxint - 1
        self.depthcount = self.depthcount + 1
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = max(v, self.my_min(s, depth, orig, alpha, beta)) #recurse
            s.undo_move(move) #undo the move when we return from the above
            if v >= beta:
                self.depthcount = self.depthcount - 1
                return v
            else:
                alpha = max(alpha, v)            
        self.depthcount = self.depthcount - 1
        return v

        
        